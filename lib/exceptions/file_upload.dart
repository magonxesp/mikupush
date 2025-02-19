class UnknownFileMimeTypeException implements Exception {

  final String message;

  const UnknownFileMimeTypeException(this.message);

  @override
  String toString() => message;

  static UnknownFileMimeTypeException forFilePath(String filePath) {
    return UnknownFileMimeTypeException('Unable to resolve mime type for file $filePath');
  }
}