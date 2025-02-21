import 'dart:async';
import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:miku_push/exceptions/upload_file_failed.dart';
import 'package:miku_push/model/file_upload_progress.dart';
import 'package:dio/dio.dart';

Future<void> uploadFile({
  required FileUploadProgress progress,
  required void Function(FileUploadProgress) onStartUpload,
  required void Function(FileUploadProgress) onUpdateProgress,
  required StreamController<String> cancelSignal,
}) async {
  debugPrint('Uploading file ${progress.filePath}');

  final dio = Dio();
  cancelSignal.stream.listen((id) {
    if (progress.id == id) dio.close();
  });

  onStartUpload(progress);

  int lastProgress = 0;
  int lastTimestamp = DateTime.now().millisecondsSinceEpoch;
  final file = File(progress.filePath);

  final options = Options(headers: {
    Headers.contentTypeHeader: progress.fileMimeType,
    Headers.contentLengthHeader: await file.length()
  });

  try {
    final response = await dio.post(
      'https://mikupush.io/upload/${progress.id}',
      data: file.openRead(),
      options: options,
      onSendProgress: (sent, total) {
        int currentTimestamp = DateTime.now().millisecondsSinceEpoch;
        int elapsedTime = currentTimestamp - lastTimestamp;

        if (elapsedTime > 1000) {
          int dataSentSinceLast = sent - lastProgress;

          progress.updateProgress(
            sent / total,
            (dataSentSinceLast / elapsedTime) * 1000,
          );
          debugPrint(progress.toString());

          lastProgress = sent;
          lastTimestamp = currentTimestamp;

          onUpdateProgress(progress);
        }
      },
    );

    if (response.statusCode != 201) {
      return Future.error(
          UploadFileFailedException.forResponse(file.path, response));
    }
  } finally {
    debugPrint('Closing http client');
    dio.close();
  }

  debugPrint('Finish uploading file $file');
}
