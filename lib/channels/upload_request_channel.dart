import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:miku_push/model/file_uploads_provider.dart';
import 'package:provider/provider.dart';

void listenChannelUploadRequests(BuildContext context) {
  debugPrint('''
    Listening uploads requests on channel method onUploadRequestReceived
  ''');

  final platform = MethodChannel('io.mikupush.app');
  final provider = Provider.of<FileUploadsProvider>(context, listen: false);

  platform.setMethodCallHandler((call) async {
    if (call.method == 'onUploadRequestReceived') {
      debugPrint('onUploadRequestReceived has called');

      if (call.arguments is String) {
        final filePath = call.arguments as String;
        debugPrint('File upload request received from channel: ${filePath}');

        provider.uploadFile(filePath);
      }
    }
  });
}
