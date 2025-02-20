import 'package:flutter/material.dart';
import 'package:miku_push/model/file_uploads_model.dart';
import 'package:miku_push/widgets/file_input.dart';
import 'package:provider/provider.dart';

class UploadFileTab extends StatelessWidget {
  const UploadFileTab({super.key});

  @override
  Widget build(BuildContext context) {
    final model = context.read<FileUploadsModel>();

    return Padding(
      padding: EdgeInsets.all(20),
      child: FileInput(
        onChange: (files) {
          model.uploadFiles(files);
        },
      ),
    );
  }
}
