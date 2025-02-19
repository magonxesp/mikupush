import 'package:http/http.dart';

class UploadFileFailedException implements Exception {

  final String message;

  const UploadFileFailedException(this.message);

  @override
  String toString() => message;

  static UploadFileFailedException forResponse(String filePath, Response response) {
    return UploadFileFailedException('''
      Failed uploading file $filePath, server sent status code ${response.statusCode}
      with body: ${response.body}
    ''');
  }
}