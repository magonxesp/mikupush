#!/bin/bash

BASE_ICON="resources/icon_1024x1024.png"
MACOS_ICONSET_DIR="resources/macOS/AppIcon.iconset"

cp "$BASE_ICON" "$MACOS_ICONSET_DIR/icon_512x512@2x.png"

magick "$BASE_ICON" -resize 512x512 "$MACOS_ICONSET_DIR/icon_512x512.png"
cp "$MACOS_ICONSET_DIR/icon_512x512.png" "$MACOS_ICONSET_DIR/icon_256x256@2x.png"

magick "$MACOS_ICONSET_DIR/icon_512x512.png" -resize 256x256 "$MACOS_ICONSET_DIR/icon_256x256.png"
cp "$MACOS_ICONSET_DIR/icon_512x512.png" "$MACOS_ICONSET_DIR/icon_256x256@2x.png"

magick "$MACOS_ICONSET_DIR/icon_256x256.png" -resize 128x128 "$MACOS_ICONSET_DIR/icon_128x128.png"
cp "$MACOS_ICONSET_DIR/icon_256x256.png" "$MACOS_ICONSET_DIR/icon_128x128@2x.png"

magick "$MACOS_ICONSET_DIR/icon_128x128.png" -resize 32x32 "$MACOS_ICONSET_DIR/icon_32x32.png"
cp "$MACOS_ICONSET_DIR/icon_128x128.png" "$MACOS_ICONSET_DIR/icon_32x32@2x.png"

magick "$MACOS_ICONSET_DIR/icon_32x32.png" -resize 16x16 "$MACOS_ICONSET_DIR/icon_16x16.png"
cp "$MACOS_ICONSET_DIR/icon_32x32.png" "$MACOS_ICONSET_DIR/icon_16x16@2x.png"

iconutil -c icns "$MACOS_ICONSET_DIR" -o resources/macOS/AppIcon.icns

WINDOWS_ICONS_DIR="resources/windows"
magick "$BASE_ICON" -resize 256x256 "$WINDOWS_ICONS_DIR/icon_256x256.png"
magick -background transparent "$WINDOWS_ICONS_DIR/icon_256x256.png" -define icon:auto-resize=16,24,32,48,64,72,96,128,256 "$WINDOWS_ICONS_DIR/icon.ico"

LINUX_ICONS_DIR="resources/linux"
magick "$BASE_ICON" -resize 512x512 "$LINUX_ICONS_DIR/icon.png"