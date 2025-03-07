import 'dart:io';

import 'package:flutter/material.dart';
import 'package:miku_push/model/file_uploads_provider.dart';
import 'package:provider/provider.dart';

final socketPort = 4040;
final socketAckMessage = 'ok';
bool _isListeningSocket = false;

void listenSocketUploadRequests(BuildContext context) async {
  if (_isListeningSocket) return;

  final provider = Provider.of<FileUploadsProvider>(context, listen: false);
  final server = await ServerSocket.bind(
    InternetAddress.loopbackIPv4,
    socketPort,
  );

  server.listen((Socket socket) {
    debugPrint('Socket client connected: $socket');

    socket.listen((List<int> data) {
      final filePath = String.fromCharCodes(data);
      debugPrint('Adding requested $filePath to upload queue from socket');

      provider.uploadFile(filePath);
      socket.write(socketAckMessage);
    });
  });

  debugPrint('Listening upload requests on socket port $socketPort');
  _isListeningSocket = true;
}
