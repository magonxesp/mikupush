import 'package:flutter/material.dart';
import 'package:miku_push/widgets/file_list_item.dart';

class UploadedFileListItem extends StatelessWidget {
  final String name;
  final String mimeType;
  final DateTime uploadedAt;

  const UploadedFileListItem({
    required this.name,
    required this.mimeType,
    required this.uploadedAt,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        FileListItemIcon(mimeType: mimeType),
        SizedBox(width: 10),
        Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            FileListItemName(name: name),
            SizedBox(width: 5),
            FileListItemUploadedAt(uploadedAt: uploadedAt),
          ],
        ),
        Spacer(),
        Icon(Icons.folder_outlined),
        Icon(
          Icons.close,
          color: theme.colorScheme.error,
        ),
      ],
    );
  }
}
