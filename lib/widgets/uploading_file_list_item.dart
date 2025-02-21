import 'package:flutter/material.dart';
import 'package:miku_push/widgets/file_list_item.dart';

class UploadingFileListItem extends StatelessWidget {
  final String name;
  final String mimeType;
  final double speed;
  final double progress;
  final bool inProgress;
  final String error;
  final void Function() onCancel;

  const UploadingFileListItem({
    required this.name,
    required this.mimeType,
    required this.speed,
    required this.progress,
    required this.error,
    required this.inProgress,
    required this.onCancel
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
            _subtitle(),
          ],
        ),
        Spacer(),
        CircularProgressIndicator(
          value: progress,
          color: theme.colorScheme.primary,
          constraints: BoxConstraints.expand(width: 30, height: 30),
        ),
        SizedBox(width: 10),
        IconButton(
          onPressed: onCancel,
          icon: Icon(
            Icons.close,
            color: theme.colorScheme.error,
          ),
        ),
      ],
    );
  }

  Widget _subtitle() {
    if (inProgress) {
      return FileListItemSpeed(speed: speed);
    } else if (!inProgress && error != '') {
      return FileListItemError(error: error);
    } else {
      return FileListItemQueued();
    }
  }
}
