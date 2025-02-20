import 'package:flutter/material.dart';
import 'package:miku_push/widgets/uploading_file_list_item.dart';
import 'package:provider/provider.dart';
import 'package:miku_push/model/file_uploads_model.dart';

class UploadingListTab extends StatelessWidget {
  const UploadingListTab({super.key});

  @override
  Widget build(BuildContext context) {
    final model = context.watch<FileUploadsModel>();

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
}
