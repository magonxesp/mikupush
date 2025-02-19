import 'package:miku_push/environment.dart';
import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart' as path;

import 'exceptions/database.dart';

Database? _database = null;

void initializeDatabase() async {
  final appDataDir = getAppDataDir();
  final databasePath = path.join(appDataDir.path, 'data.db');

  _database = await openDatabase(
    databasePath,
    version: 1,
    singleInstance: true,
  );
}

Database getDatabase() {
  if (_database == null) {
    throw DatabaseNotAvailableException('The database is not initialized');
  }

  return _database!;
}