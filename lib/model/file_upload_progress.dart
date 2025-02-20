import 'package:miku_push/model/file_upload.dart';

class FileUploadProgress extends FileUpload {
  double speed = 0;
  double progress = 0;
  bool _inProgress = false;
  bool _isFinished = false;
  String _error = '';

  FileUploadProgress({
    required super.id,
    required super.fileName,
    required super.filePath,
    required super.fileMimeType,
    required super.uploadedAt
  });

  static FileUploadProgress fromFileUpload(FileUpload fileUpload) {
    return FileUploadProgress(
      id: fileUpload.id,
      fileName: fileUpload.fileName,
      fileMimeType: fileUpload.fileMimeType,
      filePath: fileUpload.filePath,
      uploadedAt: fileUpload.uploadedAt
    );
  }

  bool get inProgress => _inProgress;
  bool get isSuccess => _isFinished && !_inProgress && _error == '';
  bool get isFailed => _isFinished && !_inProgress && _error != '';

  void finishSuccess() {
    _inProgress = false;
    _isFinished = true;
  }

  void finishFailed(String error) {
    _inProgress = false;
    _isFinished = true;
    _error = error;
  }

  FileUpload toFileUpload() {
    return this as FileUpload;
  }

  @override
  String toString() {
    return '''
      Progress: ${progress.toStringAsFixed(2)}%
      Upload Speed: ${speed.toStringAsFixed(2)} KB/s
    ''';
  }
}