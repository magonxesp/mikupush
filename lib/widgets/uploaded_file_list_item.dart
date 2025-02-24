import 'package:flutter/material.dart';
import 'package:miku_push/widgets/file_list_item.dart';

class UploadedFileListItem extends StatelessWidget {
  final String name;
  final String mimeType;
  final DateTime uploadedAt;
  final void Function() onShowInExplorer;
  final void Function() onDelete;

  const UploadedFileListItem({
    required this.name,
    required this.mimeType,
    required this.uploadedAt,
    required this.onShowInExplorer,
    required this.onDelete,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        FileListItemIcon(mimeType: mimeType),
        SizedBox(width: 10),
        Expanded(
          flex: 20,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              FileListItemName(name: name),
              SizedBox(width: 5),
              FileListItemUploadedAt(uploadedAt: uploadedAt),
            ],
          ),
        ),
        Spacer(),
        IconButton(
          onPressed: onShowInExplorer,
          icon: Icon(Icons.folder_outlined),
        ),
        SizedBox(width: 10),
        IconButton(
          onPressed: onDelete,
          icon: Icon(
            Icons.delete,
            color: theme.colorScheme.error,
          ),
        ),
      ],
    );
  }
}
