import 'dart:async';
import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:miku_push/exceptions/upload_canceled.dart';
import 'package:miku_push/exceptions/upload_file_failed.dart';
import 'package:miku_push/model/file_upload_progress.dart';

// TODO: Use Uri.parse instead
final Uri uploadUri = Uri.https('mikupush.io', 'upload');

Future<void> uploadFile({
  required File file,
  required FileUploadProgress progress,
  required void Function(FileUploadProgress) onStartUpload,
  required void Function(FileUploadProgress) onUpdateProgress,
  required StreamController<String> cancelSignal,
}) async {
  cancelSignal.stream.listen((id) {
    if (progress.id == id) throw UploadCanceledException.forId(progress.id);
  });

  Stream<List<int>> progressStream = await _createUploadProgressObserverStream(
      file: file,
      progress: progress,
      onUpdate: onUpdateProgress
  );

  final headers = {
    'Content-Type': progress.fileMimeType,
  };

  onStartUpload(progress);

  final response = await http.post(
    uploadUri,
    headers: headers,
    body: progressStream,
  );

  if (response.statusCode != 200) {
    throw UploadFileFailedException.forResponse(file.path, response);
  }
}

Future<Stream<List<int>>> _createUploadProgressObserverStream({
    required File file,
    required FileUploadProgress progress,
    required void Function(FileUploadProgress) onUpdate
}) async {
  int totalBytesSent = 0;
  int lastTime = DateTime.now().millisecondsSinceEpoch;
  final fileStream = file.openRead();
  final fileLength = await file.length();

  return fileStream.map((List<int> chunk) {
    totalBytesSent += chunk.length;

    int currentTime = DateTime.now().millisecondsSinceEpoch;
    int timeDiff = currentTime - lastTime;

    if (timeDiff > 1000) {
      progress.progress = totalBytesSent / fileLength;
      progress.speed = totalBytesSent / (timeDiff / 1000);
      debugPrint(progress.toString());
      onUpdate(progress);

      lastTime = currentTime;
    }

    return chunk;
  });
}