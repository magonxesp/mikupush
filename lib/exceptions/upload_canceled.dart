class UploadCanceledException implements Exception {

  final String message;
  final String id;

  const UploadCanceledException(this.id, this.message);

  @override
  String toString() => message;

  static UploadCanceledException forId(String id) {
    return UploadCanceledException(id, 'Upload for file with id $id is cancelled');
  }
}