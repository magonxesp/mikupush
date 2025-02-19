import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:miku_push/model/file_upload.dart';
import 'package:miku_push/model/file_upload_progress.dart';

class FileUploadsModel extends ChangeNotifier {
  List<FileUpload> _filesUploaded = [];
  List<FileUploadProgress> _filesUploading = [];
  final _cancelController = StreamController<String>();

  List<FileUpload> get filesUploaded => _filesUploaded;
  List<FileUploadProgress> get filesUploading => _filesUploading;

  void uploadFiles(List<File> files) {
    // Enqueue new files to upload and do upload on separated thread and consume
    // one by one
  }

  void cancel(String id) {
    _cancelController.sink.add(id);
  }

  void delete(String id) {

  }


}

