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
  final _cancelController = StreamController<String>();
  final _filesToUpload = Queue<(File, FileUpload, FileUploadProgress)>();
  bool _isUploadingFiles = false;

  List<FileUpload> get filesUploaded => _filesUploaded;
  List<FileUploadProgress> get filesUploading => _filesUploading;

  void uploadFiles(List<File> files) {
    debugPrint('Adding to queue files $files to upload');

    for (var file in files) {
      final upload = FileUpload.fromFile(file);
      final progress = upload.createProgress();

      _filesUploading.add(progress);
      _filesToUpload.add((file, upload, progress));
    }

    _filesUploading.sort(_sortByDateDesc);
    _startUploadFiles();
    notifyListeners();
  }

  void cancel(String id) {
    _cancelController.sink.add(id);
  }

  void delete(String id) {

  }

  void _startUploadFiles() async {
    if (_isUploadingFiles) return;
    _isUploadingFiles = true;

    while (_filesToUpload.isNotEmpty) {
      final (file, upload, progress) = _filesToUpload.removeFirst();

      try {
        await uploadFile(
          file: file,
          progress: progress,
          onStartUpload: (progress) {
            notifyListeners();
          },
          onUpdateProgress: (progress) {
            notifyListeners();
          },
          cancelSignal: _cancelController,
        );

        progress.finishSuccess();
        _saveUploadedFile(upload);
      } on Exception catch (exception) {
        progress.finishFailed(exception.toString());
      }
    }

    notifyListeners();
    _isUploadingFiles = false;
  }

  void _saveUploadedFile(FileUpload upload) {
    _filesUploaded.add(upload);
    _filesUploaded.sort(_sortByDateDesc);

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
}

