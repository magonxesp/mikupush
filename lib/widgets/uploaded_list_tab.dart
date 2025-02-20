import 'package:flutter/material.dart';
import 'package:miku_push/widgets/uploaded_file_list_item.dart';
import 'package:provider/provider.dart';

import '../model/file_uploads_model.dart';

class UploadedListTab extends StatelessWidget {
  const UploadedListTab({super.key});

  @override
  Widget build(BuildContext context) {
    final model = context.watch<FileUploadsModel>();

    return ListView.separated(
      itemCount: model.filesUploaded.length,
      padding: EdgeInsets.all(15),
      itemBuilder: (context, index) {
        final uploaded = model.filesUploaded[index];

        return UploadedFileListItem(
          name: uploaded.fileName,
          mimeType: uploaded.fileMimeType,
          uploadedAt: uploaded.uploadedAt,
        );
      },
      separatorBuilder: (context, index) {
        return Padding(
          padding: EdgeInsets.symmetric(vertical: 10),
          child: Divider(height: 1),
        );
      },
    );
  }
}
