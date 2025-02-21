import 'package:miku_push/model/file_upload.dart';

class FileUploadProgress extends FileUpload {
  double _speed = 0;
  double _progress = 0;
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

  double get progress => _progress;
  double get speed => _speed;
  bool get inProgress => _inProgress;
  bool get isSuccess => _isFinished && !_inProgress && _error == '';
  bool get isFailed => _isFinished && !_inProgress && _error != '';

  void updateProgress(double progress, double speed) {
    _progress = progress;
    _speed = speed;
    _inProgress = true;
  }

  void finishSuccess() {
    _progress = 1;
    _inProgress = false;
    _isFinished = true;
  }

  void finishFailed(String error) {
    _progress = 1;
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
      Progress: ${progress.toStringAsFixed(2)}
      Upload Speed: ${speed.toStringAsFixed(2)} B/s
    ''';
  }
}