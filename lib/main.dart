import 'dart:io';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:miku_push/model/file_uploads_provider.dart';
import 'package:miku_push/theme.dart';
import 'package:miku_push/widgets/badge_tab.dart';
import 'package:miku_push/widgets/upload_file_tab.dart';
import 'package:miku_push/widgets/uploading_list_tab.dart';
import 'package:miku_push/widgets/uploaded_list_tab.dart';
import 'package:provider/provider.dart';
import 'package:tray_manager/tray_manager.dart';
import 'package:window_manager/window_manager.dart';
import 'package:local_notifier/local_notifier.dart';

final appTitle = 'Miku Push!';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await windowManager.ensureInitialized();

  localNotifier.setup(
    appName: appTitle,
    // The parameter shortcutPolicy only works on Windows
    shortcutPolicy: ShortcutPolicy.requireCreate,
  );

  windowManager.waitUntilReadyToShow().then((_) async {
    await windowManager.setSize(const Size(500, 650));
    await windowManager.setMinimumSize(const Size(500, 650));
  });

  runApp(ChangeNotifierProvider(
    create: (context) => FileUploadsProvider(),
    child: MyApp(),
  ));
}

class MyApp extends StatefulWidget {
  @override
  _MyApp createState() => _MyApp();
}

class _MyApp extends State<MyApp> with TrayListener, WindowListener {
  @override
  void initState() {
    super.initState();

    windowManager.addListener(this);
    trayManager.addListener(this);

    _initWindow();
    _initTray();
  }

  @override
  void onWindowClose() {
    windowManager.hide();
  }

  @override
  void onTrayIconMouseDown() {
    windowManager.show();
  }

  @override
  void onTrayIconRightMouseDown() {
    trayManager.popUpContextMenu();
  }

  @override
  void dispose() {
    windowManager.removeListener(this);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final model = context.watch<FileUploadsProvider>();

    return MaterialApp(
      title: appTitle,
      debugShowCheckedModeBanner: false,
      theme: lightThemeData,
      darkTheme: darkThemeData,
      themeMode: ThemeMode.system,
      home: DefaultTabController(
        initialIndex: 0,
        length: 3,
        child: Scaffold(
          appBar: AppBar(
            toolbarHeight: 140,
            title: AppTitle(),
            bottom: TabBar(
              onTap: (index) {
                if (index == 2) {
                  model.loadAllUploadedFiles();
                  model.resetUploadedFilesCount();
                }
              },
              tabs: [
                const Tab(
                  icon: Icon(Icons.file_upload_outlined),
                  text: 'Upload file',
                ),
                BadgeTab(
                  icon: Icons.access_time_outlined,
                  text: 'Uploads in progress',
                  value: model.filesUploading.length,
                ),
                BadgeTab(
                  icon: Icons.list,
                  text: 'Uploaded files',
                  value: model.newFilesUploadedCount,
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

  void _initTray() async {
    Menu menu = Menu(items: [
      MenuItem(label: 'Mostrar', onClick: (_) => windowManager.show()),
      MenuItem(label: 'Salir', onClick: (_) => windowManager.destroy()),
    ]);

    await trayManager.setIcon(_trayIcon());
    await trayManager.setToolTip(appTitle);
    await trayManager.setContextMenu(menu);
  }

  void _initWindow() async {
    await windowManager.setPreventClose(true);
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
          'assets/icons/app-icon.png',
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

String _trayIcon() {
  if (Platform.isWindows) {
    return 'assets/icons/app-icon.ico';
  } else {
    return 'assets/icons/app-icon.png';
  }
}
