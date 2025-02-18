import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:intl/intl.dart';

class FileListItemName extends StatelessWidget {
  final String name;

  const FileListItemName({required this.name});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Text(
      name,
      style: theme.textTheme.bodyLarge,
      maxLines: 1,
      overflow: TextOverflow.ellipsis,
      textAlign: TextAlign.left,
    );
  }
}

class FileListItemUploadedAt extends StatelessWidget {
  final DateTime uploadedAt;

  const FileListItemUploadedAt({required this.uploadedAt});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Text(
      _formattedDateTime(),
      style: theme.textTheme.bodyMedium,
      maxLines: 1,
      overflow: TextOverflow.ellipsis,
    );
  }

  String _formattedDateTime() {
    String formattedDate =
    DateFormat.yMd().add_jm().format(uploadedAt); // Formato corto
    return formattedDate;
  }
}

class FileListItemIcon extends StatelessWidget {
  final String mimeType;

  const FileListItemIcon({required this.mimeType});

  @override
  Widget build(BuildContext context) {
    return SvgPicture.asset(
      _iconAsset(),
      height: 50,
    );
  }

  String _iconAsset() {
    const excelTypes = [
      'application/vnd.ms-excel',
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      'application/vnd.oasis.opendocument.spreadsheet'
    ];

    const wordTypes = [
      'application/msword',
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      'application/rtf',
      'application/vnd.oasis.opendocument.tex'
    ];

    const powerPointTypes = [
      'application/vnd.ms-powerpoint',
      'application/vnd.openxmlformats-officedocument.presentationml.presentation',
      'application/vnd.oasis.opendocument.presentation'
    ];

    const sourceCodeTypes = [
      'text/x-java-source',
      'text/x-c',
      'text/x-c++',
      'text/x-csharp',
      'text/x-python',
      'application/javascript',
      'application/x-typescript',
      'text/html',
      'text/css',
      'application/x-httpd-php',
      'text/x-ruby',
      'text/x-perl',
      'application/x-sh',
      'application/x-powershell',
      'application/sql',
      'text/x-go',
      'text/x-rust',
      'text/x-swift',
      'text/x-kotlin',
      'text/x-lua',
      'text/x-r-source',
      'text/x-matlab'
    ];

    const compressedTypes = [
      'application/zip',
      'application/x-7z-compressed',
      'application/x-rar-compressed',
      'application/gzip',
      'application/x-tar',
      'application/x-bzip2',
      'application/x-xz',
      'application/x-lzma',
      'application/x-apple-diskimage'
    ];

    final String asset;

    if (mimeType.startsWith('image/')) {
      asset = 'file-image.svg';
    } else if (mimeType.startsWith('audio/')) {
      asset = 'file-audio.svg';
    } else if (mimeType.startsWith('video/')) {
      asset = 'file-video.svg';
    } else if (excelTypes.contains(mimeType)) {
      asset = 'file-excel.svg';
    } else if (wordTypes.contains(mimeType)) {
      asset = 'file-word.svg';
    } else if (powerPointTypes.contains(mimeType)) {
      asset = 'file-powerpoint.svg';
    } else if (mimeType == 'application/pdf') {
      asset = 'file-pdf.svg';
    } else if (mimeType == 'text/plain') {
      asset = 'file-lines.svg';
    } else if (sourceCodeTypes.contains(mimeType)) {
      asset = 'file-code.svg';
    } else if (compressedTypes.contains(mimeType)) {
      asset = 'file-zipper.svg';
    } else {
      asset = 'file.svg';
    }

    return 'assets/icons/$asset';
  }
}
