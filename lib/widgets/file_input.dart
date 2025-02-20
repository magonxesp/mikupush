import 'dart:io';

import 'package:flutter/material.dart';
import 'package:dotted_border/dotted_border.dart';
import 'package:desktop_drop/desktop_drop.dart';
import 'package:file_picker/file_picker.dart';

class FileInput extends StatefulWidget {
  final void Function(List<File>) onChange;

  const FileInput({super.key, this.onChange = _defaultOnChange});

  @override
  State createState() => _FileInputState(onChange: onChange);
}

class _FileInputState extends State<FileInput> {
  bool _isActive = false;
  final void Function(List<File>) onChange;

  _FileInputState({this.onChange = _defaultOnChange});

  @override
  Widget build(BuildContext context) {
    return DropTarget(
      onDragDone: (event) {
        onChange(_filterFiles(event));
      },
      onDragEntered: (event) {
        setState(() {
          _isActive = true;
        });
      },
      onDragExited: (_) {
        setState(() {
          _isActive = false;
        });
      },
      child: _buildInput(context),
    );
  }

  Widget _buildInput(BuildContext context) {
    final theme = Theme.of(context);
    final Color color;

    if (_isActive) {
      color = theme.colorScheme.primary;
    } else {
      color = theme.colorScheme.onSurface;
    }

    return InkWell(
      onTap: _pickFile,
      child: DottedBorder(
        color: color,
        strokeWidth: 5,
        borderType: BorderType.RRect,
        radius: const Radius.circular(20),
        dashPattern: const [15, 15],
        stackFit: StackFit.expand,
        padding: EdgeInsets.all(20),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Icon(
              Icons.file_upload_outlined,
              size: 120,
              color: color,
            ),
            Text(
              'Drop your file here to upload it, or click to select a file.',
              style: theme.textTheme.bodyLarge?.copyWith(color: color),
              textAlign: TextAlign.center,
            )
          ],
        ),
      ),
    );
  }

  List<File> _filterFiles(DropDoneDetails event) {
    List<File> files = [];

    for (var item in event.files) {
      File file = File(item.path);
      if (file.statSync().type == FileSystemEntityType.file) {
        files.add(file);
      }
    }

    return files;
  }

  void _pickFile() async {
    FilePickerResult? result = await FilePicker.platform.pickFiles();

    if (result == null) {
      return;
    }

    List<File> files = [];

    for (var item in result.files) {
      if (item.path != null) {
        files.add(File(item.path!));
      }
    }

    onChange(files);
  }
}

void _defaultOnChange(_) {}
