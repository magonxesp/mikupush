import 'package:flutter/material.dart';
import 'package:miku_push/widgets/uploading_file_list_item.dart';
import 'package:provider/provider.dart';
import 'package:miku_push/model/file_uploads_model.dart';

class UploadingListTab extends StatelessWidget {
  const UploadingListTab({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final model = context.watch<FileUploadsModel>();

    if (model.filesUploading.isNotEmpty) {
      return _uploadingList(model);
    } else {
      return _emptyState(theme);
    }
  }

  Widget _uploadingList(FileUploadsModel model) {
    return ListView.separated(
      itemCount: model.filesUploading.length,
      padding: EdgeInsets.all(15),
      itemBuilder: (context, index) {
        final uploading = model.filesUploading[index];

        return UploadingFileListItem(
          name: uploading.fileName,
          mimeType: uploading.fileMimeType,
          speed: uploading.speed,
          progress: uploading.progress,
          inProgress: uploading.inProgress,
          error: uploading.error,
          onCancel: () {
            model.cancel(uploading.id);
          },
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
            'No uploads in progress yet. Try uploading a file!',
            style: theme.textTheme.bodyLarge,
            textAlign: TextAlign.center,
          )
        ],
      ),
    );
  }
}
