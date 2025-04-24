#!/bin/bash

if [[ -f "target/Miku Push Installer arm64.dmg" ]]; then
	rm "target/Miku Push Installer arm64.dmg"
fi

if [[ ! -d "target/Miku Push-darwin-arm64-dmg-content" ]]; then
	mkdir -p "target/Miku Push-darwin-arm64-dmg-content"
	cp -r "target/Miku Push-darwin-arm64/Miku Push.app" "target/Miku Push-darwin-arm64-dmg-content/"
fi

create-dmg \
  --volname "Miku Push Installer" \
  --volicon "resources/macOS/AppIcon.icns" \
  --window-pos 200 120 \
  --window-size 800 400 \
  --icon-size 100 \
  --icon "Miku Push.app" 200 190 \
  --hide-extension "Miku Push.app" \
  --app-drop-link 600 185 \
  "target/Miku Push Installer arm64.dmg" \
  "target/Miku Push-darwin-arm64-dmg-content"
