import 'dart:io';

import 'package:miku_push/model/file_upload_progress.dart';
import 'package:mime/mime.dart';
import 'package:path/path.dart';
import 'package:uuid/uuid.dart';

import '../exceptions/file_upload.dart';

class FileUpload {
  final String id;
  final String fileName;
  final String filePath;
  final String fileMimeType;
  final DateTime uploadedAt;

  FileUpload({
    required this.id,
    required this.fileName,
    required this.filePath,
    required this.fileMimeType,
    required this.uploadedAt,
  });

  static FileUpload fromFile(File file) {
    return FileUpload(
        id: Uuid().v4(),
        fileName: basename(file.path),
        filePath: file.path,
        fileMimeType: lookupMimeType(file.path, headerBytes: file.readAsBytesSync())
          ?? (throw UnknownFileMimeTypeException.forFilePath(file.path)),
        uploadedAt: DateTime.now(),
    );
  }

  FileUploadProgress createProgress() {
    return FileUploadProgress.fromFileUpload(this);
  }
}