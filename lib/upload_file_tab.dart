import 'package:flutter/material.dart';
import 'package:miku_push/file_input.dart';

class UploadFileTab extends StatelessWidget {
  const UploadFileTab({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.all(20),
      child: FileInput(),
    );
  }
}
