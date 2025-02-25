import 'dart:ui';

import 'package:flutter/material.dart';

class AppColors {
  static const caribbeanCurrent = Color(0xFF057073);
  static const tiffanyBlue = Color(0xFF9AD5D5);
  static const night = Color(0xFF0A0C0D);
  static const antiFlashWhite = Color(0xFFEAEAEB);
  static const verdigris = Color(0xFF38B0B6);
  static const richBlack = Color(0xFF020516);
  static const keppel = Color(0xFF5ABAAC);
  static const dark = Color(0xFF1C1B1F);
  static const light = Color(0xFFE6E1E5);
}

final lightThemeData = ThemeData(
  colorScheme: ColorScheme.light(
      surface: Colors.white,
      onSurface: Colors.black,
      primary: AppColors.caribbeanCurrent,
      onPrimary: Colors.white,
      secondary: AppColors.night,
      onSecondary: Colors.white,
      primaryContainer: AppColors.caribbeanCurrent,
      onPrimaryContainer: Colors.white,
      secondaryContainer: AppColors.tiffanyBlue,
      onSecondaryContainer: Colors.black,
      surfaceContainerHighest: AppColors.antiFlashWhite
  ),
);

final darkThemeData = ThemeData(
  colorScheme: ColorScheme.dark(
      surface: AppColors.dark,
      onSurface: Colors.white,
      primary: AppColors.verdigris,
      onPrimary: Colors.black,
      secondary: AppColors.richBlack,
      onSecondary: Colors.white,
      primaryContainer: AppColors.verdigris,
      onPrimaryContainer: Colors.black,
      secondaryContainer: AppColors.verdigris,
      onSecondaryContainer: Colors.black,
      surfaceContainerHighest: Colors.black
  ),
);
