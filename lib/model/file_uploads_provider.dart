import 'dart:async';
import 'dart:collection';

import 'package:drift/drift.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:miku_push/http/delete_file.dart' as server;
import 'package:miku_push/http/upload_file.dart' as server;
import 'package:miku_push/model/file_upload.dart';
import 'package:miku_push/model/file_upload_progress.dart';
import 'package:miku_push/database/database.dart';
import 'package:local_notifier/local_notifier.dart';
import 'package:path/path.dart' as path;

class FileUploadsProvider extends ChangeNotifier {
  List<FileUpload> _filesUploaded = [];
  List<FileUploadProgress> _filesUploading = [];
  final _cancelController = StreamController<String>.broadcast();
  final _filesToUpload = Queue<(FileUpload, FileUploadProgress)>();
  bool _isUploadingFiles = false;
  bool _isFilesUploadedLoaded = false;
  int _newFilesUploadedCount = 0;
  final _database = AppDatabase();

  List<FileUpload> get filesUploaded => _filesUploaded;

  List<FileUploadProgress> get filesUploading => _filesUploading;

  int get newFilesUploadedCount => _newFilesUploadedCount;

  void uploadFiles(List<String> filesPaths) {
    debugPrint('Adding to queue files $filesPaths to upload');

    for (var path in filesPaths) {
      _addFileToUploadQueue(path);
    }

    _sortRunningUploads();
    _startUploadFiles();
    notifyListeners();
  }

  void uploadFile(String filePath) async {
    debugPrint('Adding to queue file $filePath to upload');
    _addFileToUploadQueue(filePath);

    final fileName = path.basename(filePath);
    LocalNotification notification = LocalNotification(
      title: 'Uploading file $fileName 🚀',
      body: 'The file $fileName is added to the upload queue',
    );

    notification.show();

    _sortRunningUploads();
    _startUploadFiles();
    notifyListeners();
  }

  void cancel(String id) {
    _cancelController.sink.add(id);
    _filesUploading.removeWhere((progress) => progress.id == id);
    notifyListeners();
  }

  void delete(String id) async {
    try {
      final deleteQuery = _database.delete(_database.uploadedFile)
        ..where((t) => t.uuid.equals(id));

      await server.deleteFile(id);
      await deleteQuery.go();
      filesUploaded.removeWhere((uploaded) => uploaded.id == id);
      notifyListeners();
    } catch (exception) {
      debugPrint('Failed deleting file with $id: $exception');
    }
  }

  void loadAllUploadedFiles() async {
    if (_isFilesUploadedLoaded) {
      return;
    }

    debugPrint('Loading saved uploaded files');

    final query = _database.select(_database.uploadedFile)
      ..orderBy([
        (t) => OrderingTerm(expression: t.uploadedAt, mode: OrderingMode.desc)
      ]);

    List<UploadedFileData> all = await query.get();

    _filesUploaded = all.map((uploadedFile) {
      return FileUpload(
        id: uploadedFile.uuid,
        fileName: uploadedFile.fileName,
        filePath: uploadedFile.filePath,
        fileMimeType: uploadedFile.fileMimeType,
        uploadedAt: uploadedFile.uploadedAt,
      );
    }).toList();

    notifyListeners();
    _isFilesUploadedLoaded = true;
  }

  void retry(FileUploadProgress progress) {
    progress.reset();
    _filesToUpload.add((progress.toFileUpload(), progress));
    _sortRunningUploads();
    _startUploadFiles();
    notifyListeners();
  }

  Future<void> resetUploadedFilesCount() async {
    Future.delayed(Duration(seconds: 1));
    _newFilesUploadedCount = 0;
    notifyListeners();
  }

  void copyLink(String id) async {
    debugPrint('Copy link of file with id $id to clipboard');
    await Clipboard.setData(ClipboardData(text: 'http://localhost:8080/$id'));

    final query = _database.select(_database.uploadedFile)
      ..where((t) => t.uuid.equals(id));

    final uploadedFile = await query.getSingleOrNull();
    if (uploadedFile != null) {
      LocalNotification notification = LocalNotification(
        title: '📎Link copied to clipboard!',
        body: 'The link for ${uploadedFile.fileName} is in the clipboard',
      );

      await notification.show();
    }
  }

  void _addFileToUploadQueue(String filePath) {
    final upload = FileUpload.fromFilePath(filePath);
    final progress = upload.createProgress();

    _filesUploading.add(progress);
    _filesToUpload.add((upload, progress));
  }

  void _startUploadFiles() async {
    if (_isUploadingFiles) return;
    debugPrint('Starting uploading incoming files');
    _isUploadingFiles = true;

    while (_filesToUpload.isNotEmpty) {
      final (upload, progress) = _filesToUpload.removeFirst();

      try {
        await server.uploadFile(
          progress: progress,
          onStartUpload: (progress) {
            notifyListeners();
          },
          onUpdateProgress: (progress) {
            _filesUploading.sort(_sortQueuedLast);
            notifyListeners();
          },
          cancelSignal: _cancelController,
        );

        _handleUploadSuccess(upload, progress);
      } catch (exception) {
        debugPrint('Failed uploading file ${progress.filePath}: $exception');
        _handleUploadError(exception, progress);
      }
    }

    _isUploadingFiles = false;
  }

  void _handleUploadSuccess(FileUpload upload, FileUploadProgress progress) {
    progress.finishSuccess();
    _filesUploading.removeWhere((progress) => progress.id == upload.id);
    _filesUploaded.add(upload);
    _filesUploaded.sort(_sortByDateDesc);
    _newFilesUploadedCount++;

    notifyListeners();

    _database.into(_database.uploadedFile).insert(UploadedFileCompanion.insert(
          uuid: upload.id,
          fileName: upload.fileName,
          filePath: upload.filePath,
          fileMimeType: upload.fileMimeType,
          uploadedAt: upload.uploadedAt,
        ));
  }

  void _handleUploadError(dynamic exception, FileUploadProgress progress) {
    progress.finishFailed(exception.toString());
    notifyListeners();
  }

  void _sortRunningUploads() {
    _filesUploading.sort(_sortByDateDesc);
    _filesUploading.sort(_sortQueuedLast);
  }

  static int _sortByDateDesc(FileUpload uploadA, FileUpload uploadB) {
    final uploadAMillis = uploadA.uploadedAt.millisecondsSinceEpoch;
    final uploadBMillis = uploadB.uploadedAt.millisecondsSinceEpoch;

    if (uploadAMillis > uploadBMillis) {
      return -1;
    } else if (uploadAMillis < uploadBMillis) {
      return 1;
    } else {
      return 0;
    }
  }

  static int _sortQueuedLast(
      FileUploadProgress uploadA, FileUploadProgress uploadB) {
    final uploadAProgress = uploadA.inProgress;
    final uploadBProgress = uploadB.inProgress;

    if (uploadAProgress && !uploadBProgress) {
      return -1;
    } else if (!uploadAProgress && uploadBProgress) {
      return 1;
    } else {
      return 0;
    }
  }
}
