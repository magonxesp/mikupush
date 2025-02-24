import 'package:drift/drift.dart';

class UploadedFile extends Table {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get uuid => text()();
  TextColumn get fileName => text()();
  TextColumn get filePath => text()();
  TextColumn get fileMimeType => text()();
  DateTimeColumn get uploadedAt => dateTime()();
}
