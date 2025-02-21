class DatabaseNotAvailableException implements Exception {
  final String message;

  const DatabaseNotAvailableException(this.message);

  @override
  String toString() => message;
}