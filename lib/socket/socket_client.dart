import 'dart:io';
import 'package:flutter/material.dart';
import 'package:miku_push/socket/socket_server.dart';
import 'package:miku_push/exceptions/socket_ack_missing.dart';

Future<void> sendUploadRequest(String filePath) async {
  final socket = await Socket.connect(InternetAddress.loopbackIPv4, socketPort);
  debugPrint('Connecting to socket');

  socket.write(filePath);

  final response = await socket.first.timeout(Duration(milliseconds: 250));
  final ack = String.fromCharCodes(response);
  debugPrint('Received response from socket server $response');

  if (ack != socketAckMessage) {
    throw SocketAckMissingException();
  }

  debugPrint('Socket server ack received');
  socket.destroy();
}
