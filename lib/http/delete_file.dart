import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:miku_push/exceptions/delete_file_failed.dart';

Future<void> deleteFile(String id) async {
  debugPrint('Deleting file with id $id');

  final dio = Dio();

  try {
    final response = await dio.delete('http://localhost:8080/$id');

    if (response.statusCode != 200) {
      return Future.error(DeleteFileFailedException.forResponse(id, response));
    }
  } finally {
    debugPrint('Closing http client');
    dio.close();
  }

  debugPrint('Finish deleting file with id $id');
}
