import 'package:flutter/material.dart';
import 'package:miku_push/uploaded_file_list_item.dart';

class UploadedListTab extends StatelessWidget {
  const UploadedListTab({super.key});

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      itemCount: 10,
      padding: EdgeInsets.all(15),
      itemBuilder: (context, index) {
        return UploadedFileListItem(
          name: 'alya.jpg',
          mimeType: 'image/jpeg',
          uploadedAt: DateTime.now(),
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
