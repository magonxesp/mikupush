import 'package:dio/dio.dart';

class DeleteFileFailedException implements Exception {

  final String message;

  const DeleteFileFailedException(this.message);

  @override
  String toString() => message;

  static DeleteFileFailedException forResponse(String id, Response<dynamic> response) {
    return DeleteFileFailedException('Failed deleting file with id $id, server sent status code ${response.statusCode}');
  }
}