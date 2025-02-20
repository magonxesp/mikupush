import 'package:flutter/material.dart';
import 'package:miku_push/widgets/uploaded_file_list_item.dart';
import 'package:provider/provider.dart';

import '../model/file_uploads_model.dart';

class UploadedListTab extends StatelessWidget {
  const UploadedListTab({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final model = context.watch<FileUploadsModel>();

    if (model.filesUploaded.isNotEmpty) {
      return _uploadedList(model);
    } else {
      return _emptyState(theme);
    }
  }

  Widget _uploadedList(FileUploadsModel model) {
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

  Widget _emptyState(ThemeData theme) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Icon(Icons.inventory_2),
          SizedBox(height: 15),
          Text(
            'No files uploaded yet. Try uploading one!',
            style: theme.textTheme.bodyLarge,
            textAlign: TextAlign.center,
          )
        ],
      ),
    );
  }
}
