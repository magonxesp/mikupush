import 'package:flutter/material.dart';
import 'package:miku_push/widgets/upload_file_tab.dart';
import 'package:miku_push/widgets/uploading_list_tab.dart';
import 'package:miku_push/widgets/uploaded_list_tab.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: DefaultTabController(
        initialIndex: 1,
        length: 3,
        child: Scaffold(
          appBar: AppBar(
            toolbarHeight: 120,
            title: AppTitle(),
            bottom: const TabBar(
              tabs: [
                Tab(
                  icon: Icon(Icons.file_upload_outlined),
                  text: 'Upload file',
                ),
                Tab(
                  icon: Icon(Icons.access_time_outlined),
                  text: 'Files being uploaded',
                ),
                Tab(
                  icon: Icon(Icons.list),
                  text: 'Uploaded files',
                ),
              ],
            ),
          ),
          body: const TabBarView(
            children: [
              const UploadFileTab(),
              const UploadingListTab(),
              const UploadedListTab(),
            ],
          ),
        ),
      ),
    );
  }
}

class AppTitle extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Image.asset(
          'assets/icons/app_icon.png',
          height: 100,
          fit: BoxFit.contain,
        ),
        SizedBox(width: 15),
        Text(
          'Miku Push!',
          style: theme.textTheme.titleLarge?.copyWith(
            fontFamily: 'Fredoka',
            fontWeight: FontWeight.bold,
            fontSize: 60,
            color: theme.colorScheme.primary,
          ),
          overflow: TextOverflow.ellipsis,
        )
      ],
    );
  }
}