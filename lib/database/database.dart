import 'package:drift/drift.dart';
import 'package:drift_flutter/drift_flutter.dart';
import 'package:miku_push/database/uploaded_file_table.dart';
import 'package:miku_push/environment.dart';
import 'package:path/path.dart' as path;

part 'database.g.dart';

@DriftDatabase(tables: [UploadedFile])
class AppDatabase extends _$AppDatabase {
  AppDatabase() : super(_openConnection());

  @override
  // TODO: implement schemaVersion
  int get schemaVersion => 1;

  static QueryExecutor _openConnection() {
    return driftDatabase(
      name: 'miku_push_data',
      native: const DriftNativeOptions(
        databaseDirectory: _getDatabaseDirectory
      ),
    );
  }

  static Future<Object> _getDatabaseDirectory() async {
    return path.join(getAppDataDir().path, 'database.db');
  }
}