import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:path/path.dart';

Directory getAppDataDir() {
  final String path;

  if (Platform.isWindows) {
    path = join(Platform.environment['APPDATA']!, 'MikuPush');
  } else if (Platform.isMacOS) {
    path = join(Platform.environment['HOME']!, 'Library', 'Application Support', 'io.mikupush.app');
  } else {
    path = join(Platform.environment['HOME']!, '.mikupush');
  }

  final directory = Directory(path);

  if (!directory.existsSync()) {
    debugPrint('The $path directory not exists, creating it');
    directory.create(recursive: true);
  }

  return directory;
}