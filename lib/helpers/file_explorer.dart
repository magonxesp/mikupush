import 'dart:io';

import 'package:flutter/material.dart';

void showFileInExplorer(String filePath) async {
  debugPrint('Showing $filePath on file explorer');

  if (Platform.isWindows) {
    Process.run('powershell', ['-command', 'explorer /select,"$filePath"']);
  } else if (Platform.isMacOS) {
    Process.run('open', ['-R', filePath]);
  } else if (Platform.isLinux) {
    Process.run('xdg-open', [filePath]);
  }
}