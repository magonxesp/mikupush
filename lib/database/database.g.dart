// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'database.dart';

// ignore_for_file: type=lint
class $UploadedFileTable extends UploadedFile
    with TableInfo<$UploadedFileTable, UploadedFileData> {
  @override
  final GeneratedDatabase attachedDatabase;
  final String? _alias;
  $UploadedFileTable(this.attachedDatabase, [this._alias]);
  static const VerificationMeta _idMeta = const VerificationMeta('id');
  @override
  late final GeneratedColumn<int> id = GeneratedColumn<int>(
      'id', aliasedName, false,
      hasAutoIncrement: true,
      type: DriftSqlType.int,
      requiredDuringInsert: false,
      defaultConstraints:
          GeneratedColumn.constraintIsAlways('PRIMARY KEY AUTOINCREMENT'));
  static const VerificationMeta _uuidMeta = const VerificationMeta('uuid');
  @override
  late final GeneratedColumn<String> uuid = GeneratedColumn<String>(
      'uuid', aliasedName, false,
      type: DriftSqlType.string, requiredDuringInsert: true);
  static const VerificationMeta _fileNameMeta =
      const VerificationMeta('fileName');
  @override
  late final GeneratedColumn<String> fileName = GeneratedColumn<String>(
      'file_name', aliasedName, false,
      type: DriftSqlType.string, requiredDuringInsert: true);
  static const VerificationMeta _filePathMeta =
      const VerificationMeta('filePath');
  @override
  late final GeneratedColumn<String> filePath = GeneratedColumn<String>(
      'file_path', aliasedName, false,
      type: DriftSqlType.string, requiredDuringInsert: true);
  static const VerificationMeta _fileMimeTypeMeta =
      const VerificationMeta('fileMimeType');
  @override
  late final GeneratedColumn<String> fileMimeType = GeneratedColumn<String>(
      'file_mime_type', aliasedName, false,
      type: DriftSqlType.string, requiredDuringInsert: true);
  static const VerificationMeta _uploadedAtMeta =
      const VerificationMeta('uploadedAt');
  @override
  late final GeneratedColumn<DateTime> uploadedAt = GeneratedColumn<DateTime>(
      'uploaded_at', aliasedName, false,
      type: DriftSqlType.dateTime, requiredDuringInsert: true);
  @override
  List<GeneratedColumn> get $columns =>
      [id, uuid, fileName, filePath, fileMimeType, uploadedAt];
  @override
  String get aliasedName => _alias ?? actualTableName;
  @override
  String get actualTableName => $name;
  static const String $name = 'uploaded_file';
  @override
  VerificationContext validateIntegrity(Insertable<UploadedFileData> instance,
      {bool isInserting = false}) {
    final context = VerificationContext();
    final data = instance.toColumns(true);
    if (data.containsKey('id')) {
      context.handle(_idMeta, id.isAcceptableOrUnknown(data['id']!, _idMeta));
    }
    if (data.containsKey('uuid')) {
      context.handle(
          _uuidMeta, uuid.isAcceptableOrUnknown(data['uuid']!, _uuidMeta));
    } else if (isInserting) {
      context.missing(_uuidMeta);
    }
    if (data.containsKey('file_name')) {
      context.handle(_fileNameMeta,
          fileName.isAcceptableOrUnknown(data['file_name']!, _fileNameMeta));
    } else if (isInserting) {
      context.missing(_fileNameMeta);
    }
    if (data.containsKey('file_path')) {
      context.handle(_filePathMeta,
          filePath.isAcceptableOrUnknown(data['file_path']!, _filePathMeta));
    } else if (isInserting) {
      context.missing(_filePathMeta);
    }
    if (data.containsKey('file_mime_type')) {
      context.handle(
          _fileMimeTypeMeta,
          fileMimeType.isAcceptableOrUnknown(
              data['file_mime_type']!, _fileMimeTypeMeta));
    } else if (isInserting) {
      context.missing(_fileMimeTypeMeta);
    }
    if (data.containsKey('uploaded_at')) {
      context.handle(
          _uploadedAtMeta,
          uploadedAt.isAcceptableOrUnknown(
              data['uploaded_at']!, _uploadedAtMeta));
    } else if (isInserting) {
      context.missing(_uploadedAtMeta);
    }
    return context;
  }

  @override
  Set<GeneratedColumn> get $primaryKey => {id};
  @override
  UploadedFileData map(Map<String, dynamic> data, {String? tablePrefix}) {
    final effectivePrefix = tablePrefix != null ? '$tablePrefix.' : '';
    return UploadedFileData(
      id: attachedDatabase.typeMapping
          .read(DriftSqlType.int, data['${effectivePrefix}id'])!,
      uuid: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}uuid'])!,
      fileName: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}file_name'])!,
      filePath: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}file_path'])!,
      fileMimeType: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}file_mime_type'])!,
      uploadedAt: attachedDatabase.typeMapping
          .read(DriftSqlType.dateTime, data['${effectivePrefix}uploaded_at'])!,
    );
  }

  @override
  $UploadedFileTable createAlias(String alias) {
    return $UploadedFileTable(attachedDatabase, alias);
  }
}

class UploadedFileData extends DataClass
    implements Insertable<UploadedFileData> {
  final int id;
  final String uuid;
  final String fileName;
  final String filePath;
  final String fileMimeType;
  final DateTime uploadedAt;
  const UploadedFileData(
      {required this.id,
      required this.uuid,
      required this.fileName,
      required this.filePath,
      required this.fileMimeType,
      required this.uploadedAt});
  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    map['id'] = Variable<int>(id);
    map['uuid'] = Variable<String>(uuid);
    map['file_name'] = Variable<String>(fileName);
    map['file_path'] = Variable<String>(filePath);
    map['file_mime_type'] = Variable<String>(fileMimeType);
    map['uploaded_at'] = Variable<DateTime>(uploadedAt);
    return map;
  }

  UploadedFileCompanion toCompanion(bool nullToAbsent) {
    return UploadedFileCompanion(
      id: Value(id),
      uuid: Value(uuid),
      fileName: Value(fileName),
      filePath: Value(filePath),
      fileMimeType: Value(fileMimeType),
      uploadedAt: Value(uploadedAt),
    );
  }

  factory UploadedFileData.fromJson(Map<String, dynamic> json,
      {ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return UploadedFileData(
      id: serializer.fromJson<int>(json['id']),
      uuid: serializer.fromJson<String>(json['uuid']),
      fileName: serializer.fromJson<String>(json['fileName']),
      filePath: serializer.fromJson<String>(json['filePath']),
      fileMimeType: serializer.fromJson<String>(json['fileMimeType']),
      uploadedAt: serializer.fromJson<DateTime>(json['uploadedAt']),
    );
  }
  @override
  Map<String, dynamic> toJson({ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return <String, dynamic>{
      'id': serializer.toJson<int>(id),
      'uuid': serializer.toJson<String>(uuid),
      'fileName': serializer.toJson<String>(fileName),
      'filePath': serializer.toJson<String>(filePath),
      'fileMimeType': serializer.toJson<String>(fileMimeType),
      'uploadedAt': serializer.toJson<DateTime>(uploadedAt),
    };
  }

  UploadedFileData copyWith(
          {int? id,
          String? uuid,
          String? fileName,
          String? filePath,
          String? fileMimeType,
          DateTime? uploadedAt}) =>
      UploadedFileData(
        id: id ?? this.id,
        uuid: uuid ?? this.uuid,
        fileName: fileName ?? this.fileName,
        filePath: filePath ?? this.filePath,
        fileMimeType: fileMimeType ?? this.fileMimeType,
        uploadedAt: uploadedAt ?? this.uploadedAt,
      );
  UploadedFileData copyWithCompanion(UploadedFileCompanion data) {
    return UploadedFileData(
      id: data.id.present ? data.id.value : this.id,
      uuid: data.uuid.present ? data.uuid.value : this.uuid,
      fileName: data.fileName.present ? data.fileName.value : this.fileName,
      filePath: data.filePath.present ? data.filePath.value : this.filePath,
      fileMimeType: data.fileMimeType.present
          ? data.fileMimeType.value
          : this.fileMimeType,
      uploadedAt:
          data.uploadedAt.present ? data.uploadedAt.value : this.uploadedAt,
    );
  }

  @override
  String toString() {
    return (StringBuffer('UploadedFileData(')
          ..write('id: $id, ')
          ..write('uuid: $uuid, ')
          ..write('fileName: $fileName, ')
          ..write('filePath: $filePath, ')
          ..write('fileMimeType: $fileMimeType, ')
          ..write('uploadedAt: $uploadedAt')
          ..write(')'))
        .toString();
  }

  @override
  int get hashCode =>
      Object.hash(id, uuid, fileName, filePath, fileMimeType, uploadedAt);
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is UploadedFileData &&
          other.id == this.id &&
          other.uuid == this.uuid &&
          other.fileName == this.fileName &&
          other.filePath == this.filePath &&
          other.fileMimeType == this.fileMimeType &&
          other.uploadedAt == this.uploadedAt);
}

class UploadedFileCompanion extends UpdateCompanion<UploadedFileData> {
  final Value<int> id;
  final Value<String> uuid;
  final Value<String> fileName;
  final Value<String> filePath;
  final Value<String> fileMimeType;
  final Value<DateTime> uploadedAt;
  const UploadedFileCompanion({
    this.id = const Value.absent(),
    this.uuid = const Value.absent(),
    this.fileName = const Value.absent(),
    this.filePath = const Value.absent(),
    this.fileMimeType = const Value.absent(),
    this.uploadedAt = const Value.absent(),
  });
  UploadedFileCompanion.insert({
    this.id = const Value.absent(),
    required String uuid,
    required String fileName,
    required String filePath,
    required String fileMimeType,
    required DateTime uploadedAt,
  })  : uuid = Value(uuid),
        fileName = Value(fileName),
        filePath = Value(filePath),
        fileMimeType = Value(fileMimeType),
        uploadedAt = Value(uploadedAt);
  static Insertable<UploadedFileData> custom({
    Expression<int>? id,
    Expression<String>? uuid,
    Expression<String>? fileName,
    Expression<String>? filePath,
    Expression<String>? fileMimeType,
    Expression<DateTime>? uploadedAt,
  }) {
    return RawValuesInsertable({
      if (id != null) 'id': id,
      if (uuid != null) 'uuid': uuid,
      if (fileName != null) 'file_name': fileName,
      if (filePath != null) 'file_path': filePath,
      if (fileMimeType != null) 'file_mime_type': fileMimeType,
      if (uploadedAt != null) 'uploaded_at': uploadedAt,
    });
  }

  UploadedFileCompanion copyWith(
      {Value<int>? id,
      Value<String>? uuid,
      Value<String>? fileName,
      Value<String>? filePath,
      Value<String>? fileMimeType,
      Value<DateTime>? uploadedAt}) {
    return UploadedFileCompanion(
      id: id ?? this.id,
      uuid: uuid ?? this.uuid,
      fileName: fileName ?? this.fileName,
      filePath: filePath ?? this.filePath,
      fileMimeType: fileMimeType ?? this.fileMimeType,
      uploadedAt: uploadedAt ?? this.uploadedAt,
    );
  }

  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    if (id.present) {
      map['id'] = Variable<int>(id.value);
    }
    if (uuid.present) {
      map['uuid'] = Variable<String>(uuid.value);
    }
    if (fileName.present) {
      map['file_name'] = Variable<String>(fileName.value);
    }
    if (filePath.present) {
      map['file_path'] = Variable<String>(filePath.value);
    }
    if (fileMimeType.present) {
      map['file_mime_type'] = Variable<String>(fileMimeType.value);
    }
    if (uploadedAt.present) {
      map['uploaded_at'] = Variable<DateTime>(uploadedAt.value);
    }
    return map;
  }

  @override
  String toString() {
    return (StringBuffer('UploadedFileCompanion(')
          ..write('id: $id, ')
          ..write('uuid: $uuid, ')
          ..write('fileName: $fileName, ')
          ..write('filePath: $filePath, ')
          ..write('fileMimeType: $fileMimeType, ')
          ..write('uploadedAt: $uploadedAt')
          ..write(')'))
        .toString();
  }
}

abstract class _$AppDatabase extends GeneratedDatabase {
  _$AppDatabase(QueryExecutor e) : super(e);
  $AppDatabaseManager get managers => $AppDatabaseManager(this);
  late final $UploadedFileTable uploadedFile = $UploadedFileTable(this);
  @override
  Iterable<TableInfo<Table, Object?>> get allTables =>
      allSchemaEntities.whereType<TableInfo<Table, Object?>>();
  @override
  List<DatabaseSchemaEntity> get allSchemaEntities => [uploadedFile];
}

typedef $$UploadedFileTableCreateCompanionBuilder = UploadedFileCompanion
    Function({
  Value<int> id,
  required String uuid,
  required String fileName,
  required String filePath,
  required String fileMimeType,
  required DateTime uploadedAt,
});
typedef $$UploadedFileTableUpdateCompanionBuilder = UploadedFileCompanion
    Function({
  Value<int> id,
  Value<String> uuid,
  Value<String> fileName,
  Value<String> filePath,
  Value<String> fileMimeType,
  Value<DateTime> uploadedAt,
});

class $$UploadedFileTableFilterComposer
    extends Composer<_$AppDatabase, $UploadedFileTable> {
  $$UploadedFileTableFilterComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnFilters<int> get id => $composableBuilder(
      column: $table.id, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get uuid => $composableBuilder(
      column: $table.uuid, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get fileName => $composableBuilder(
      column: $table.fileName, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get filePath => $composableBuilder(
      column: $table.filePath, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get fileMimeType => $composableBuilder(
      column: $table.fileMimeType, builder: (column) => ColumnFilters(column));

  ColumnFilters<DateTime> get uploadedAt => $composableBuilder(
      column: $table.uploadedAt, builder: (column) => ColumnFilters(column));
}

class $$UploadedFileTableOrderingComposer
    extends Composer<_$AppDatabase, $UploadedFileTable> {
  $$UploadedFileTableOrderingComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnOrderings<int> get id => $composableBuilder(
      column: $table.id, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get uuid => $composableBuilder(
      column: $table.uuid, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get fileName => $composableBuilder(
      column: $table.fileName, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get filePath => $composableBuilder(
      column: $table.filePath, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get fileMimeType => $composableBuilder(
      column: $table.fileMimeType,
      builder: (column) => ColumnOrderings(column));

  ColumnOrderings<DateTime> get uploadedAt => $composableBuilder(
      column: $table.uploadedAt, builder: (column) => ColumnOrderings(column));
}

class $$UploadedFileTableAnnotationComposer
    extends Composer<_$AppDatabase, $UploadedFileTable> {
  $$UploadedFileTableAnnotationComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  GeneratedColumn<int> get id =>
      $composableBuilder(column: $table.id, builder: (column) => column);

  GeneratedColumn<String> get uuid =>
      $composableBuilder(column: $table.uuid, builder: (column) => column);

  GeneratedColumn<String> get fileName =>
      $composableBuilder(column: $table.fileName, builder: (column) => column);

  GeneratedColumn<String> get filePath =>
      $composableBuilder(column: $table.filePath, builder: (column) => column);

  GeneratedColumn<String> get fileMimeType => $composableBuilder(
      column: $table.fileMimeType, builder: (column) => column);

  GeneratedColumn<DateTime> get uploadedAt => $composableBuilder(
      column: $table.uploadedAt, builder: (column) => column);
}

class $$UploadedFileTableTableManager extends RootTableManager<
    _$AppDatabase,
    $UploadedFileTable,
    UploadedFileData,
    $$UploadedFileTableFilterComposer,
    $$UploadedFileTableOrderingComposer,
    $$UploadedFileTableAnnotationComposer,
    $$UploadedFileTableCreateCompanionBuilder,
    $$UploadedFileTableUpdateCompanionBuilder,
    (
      UploadedFileData,
      BaseReferences<_$AppDatabase, $UploadedFileTable, UploadedFileData>
    ),
    UploadedFileData,
    PrefetchHooks Function()> {
  $$UploadedFileTableTableManager(_$AppDatabase db, $UploadedFileTable table)
      : super(TableManagerState(
          db: db,
          table: table,
          createFilteringComposer: () =>
              $$UploadedFileTableFilterComposer($db: db, $table: table),
          createOrderingComposer: () =>
              $$UploadedFileTableOrderingComposer($db: db, $table: table),
          createComputedFieldComposer: () =>
              $$UploadedFileTableAnnotationComposer($db: db, $table: table),
          updateCompanionCallback: ({
            Value<int> id = const Value.absent(),
            Value<String> uuid = const Value.absent(),
            Value<String> fileName = const Value.absent(),
            Value<String> filePath = const Value.absent(),
            Value<String> fileMimeType = const Value.absent(),
            Value<DateTime> uploadedAt = const Value.absent(),
          }) =>
              UploadedFileCompanion(
            id: id,
            uuid: uuid,
            fileName: fileName,
            filePath: filePath,
            fileMimeType: fileMimeType,
            uploadedAt: uploadedAt,
          ),
          createCompanionCallback: ({
            Value<int> id = const Value.absent(),
            required String uuid,
            required String fileName,
            required String filePath,
            required String fileMimeType,
            required DateTime uploadedAt,
          }) =>
              UploadedFileCompanion.insert(
            id: id,
            uuid: uuid,
            fileName: fileName,
            filePath: filePath,
            fileMimeType: fileMimeType,
            uploadedAt: uploadedAt,
          ),
          withReferenceMapper: (p0) => p0
              .map((e) => (e.readTable(table), BaseReferences(db, table, e)))
              .toList(),
          prefetchHooksCallback: null,
        ));
}

typedef $$UploadedFileTableProcessedTableManager = ProcessedTableManager<
    _$AppDatabase,
    $UploadedFileTable,
    UploadedFileData,
    $$UploadedFileTableFilterComposer,
    $$UploadedFileTableOrderingComposer,
    $$UploadedFileTableAnnotationComposer,
    $$UploadedFileTableCreateCompanionBuilder,
    $$UploadedFileTableUpdateCompanionBuilder,
    (
      UploadedFileData,
      BaseReferences<_$AppDatabase, $UploadedFileTable, UploadedFileData>
    ),
    UploadedFileData,
    PrefetchHooks Function()>;

class $AppDatabaseManager {
  final _$AppDatabase _db;
  $AppDatabaseManager(this._db);
  $$UploadedFileTableTableManager get uploadedFile =>
      $$UploadedFileTableTableManager(_db, _db.uploadedFile);
}
