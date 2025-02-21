import 'dart:async';
import 'dart:collection';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:miku_push/http/upload_file.dart';
import 'package:miku_push/model/file_upload.dart';
import 'package:miku_push/model/file_upload_progress.dart';

class FileUploadsModel extends ChangeNotifier {
  List<FileUpload> _filesUploaded = [];
  List<FileUploadProgress> _filesUploading = [];
  final _cancelController = StreamController<String>.broadcast();
  final _filesToUpload = Queue<(FileUpload, FileUploadProgress)>();
  bool _isUploadingFiles = false;

  List<FileUpload> get filesUploaded => _filesUploaded;
  List<FileUploadProgress> get filesUploading => _filesUploading;

  void uploadFiles(List<String> filesPaths) {
    debugPrint('Adding to queue files $filesPaths to upload');

    for (var path in filesPaths) {
      final upload = FileUpload.fromFilePath(path);
      final progress = upload.createProgress();

      _filesUploading.add(progress);
      _filesToUpload.add((upload, progress));
    }

    _filesUploading.sort(_sortByDateDesc);
    _filesUploading.sort(_sortQueuedLast);
    _startUploadFiles();
    notifyListeners();
  }

  void cancel(String id) {
    _cancelController.sink.add(id);
    _filesUploading.removeWhere((progress) => progress.id == id);
    notifyListeners();
  }

  void delete(String id) {

  }

  void _startUploadFiles() async {
    if (_isUploadingFiles) return;
    debugPrint('Starting uploading incoming files');
    _isUploadingFiles = true;

    while (_filesToUpload.isNotEmpty) {
      final (upload, progress) = _filesToUpload.removeFirst();

      try {
        await uploadFile(
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

    notifyListeners();
  }

  void _handleUploadError(dynamic exception, FileUploadProgress progress) {
    progress.finishFailed(exception.toString());
    notifyListeners();
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

  static int _sortQueuedLast(FileUploadProgress uploadA, FileUploadProgress uploadB) {
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

