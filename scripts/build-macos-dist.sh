#!/bin/bash

rm -rf dist

DEBUG_DIST_DIR="dist/debug/$(bash scripts/target-name.sh)"
RELEASE_DIST_DIR="dist/release/$(bash scripts/target-name.sh)"

if [[ "$1" == "--debug" ]]; then
  DIST_DIR="$DEBUG_DIST_DIR"
  BUILD_DESTINATION="build/macos/Build/Products/Debug/Miku Push.app"
  BUILD_ARGS=(--debug)
else
  DIST_DIR="$RELEASE_DIST_DIR"
  BUILD_DESTINATION="build/macos/Build/Products/Release/Miku Push.app"
  BUILD_ARGS=(--release)
fi

mkdir -p "$DIST_DIR"
flutter build macos ${BUILD_ARGS[@]}
mv "$BUILD_DESTINATION" "$DIST_DIR/Miku Push.app"

export GOPATH="$PWD/helper"
export GO111MODULE=off
go build -o "$DIST_DIR/Miku Push.app/Contents/MacOS/Miku Push Helper" ./helper

create-dmg \
  --volname "Miku Push Installer" \
  --window-pos 200 120 \
  --window-size 800 400 \
  --icon-size 100 \
  --icon "Miku Push.app" 200 190 \
  --hide-extension "Miku Push.app" \
  --app-drop-link 600 185 \
  "$DIST_DIR/Miku Push Installer.dmg" \
  "$DIST_DIR/"