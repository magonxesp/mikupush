.ONESHELL:
SHELL := bash
.SHELLFLAGS := -e -o pipefail -c

.PHONY: \
	build-server \
	build-app-macos \
	build-app-windows \
	build-app-linux \
	build

build-server:
	cd server
	[[ -d target ]] && rm -r target
	GOOS=windows GOARCH=arm64 go build -o target/server-windows-arm64.exe
	GOOS=windows GOARCH=amd64 go build -o target/server-windows-amd64.exe
	GOOS=darwin GOARCH=arm64 go build -o target/server-darwin-arm64
	GOOS=darwin GOARCH=amd64 go build -o target/server-darwin-amd64
	GOOS=linux GOARCH=arm64 go build -o target/server-linux-arm64
	GOOS=linux GOARCH=amd64 go build -o target/server-linux-amd64

app/node_modules:
	cd app
	npm install

app/dist-electron: app/node_modules
	cd app
	npm run build:prod

build-app-macos: app/dist-electron
	cd app
	npm run package:macos
	cd target
	[[ -f 'Miku Push-darwin-arm64.zip' ]] && rm 'Miku Push-darwin-arm64.zip'
	[[ -f 'Miku Push-darwin-x64.zip' ]] && rm 'Miku Push-darwin-x64.zip'
	zip -r 'Miku Push-darwin-arm64.zip' 'Miku Push-darwin-arm64'
	zip -r 'Miku Push-darwin-x64.zip' 'Miku Push-darwin-x64'

build-app-windows: app/dist-electron
	cd app
	npm run package:windows
	cd target
	[[ -f 'Miku Push-win32-arm64.zip' ]] && rm 'Miku Push-win32-arm64.zip'
	[[ -f 'Miku Push-win32-x64.zip' ]] && rm 'Miku Push-win32-x64.zip'
	zip -r 'Miku Push-win32-arm64.zip' 'Miku Push-win32-arm64'
	zip -r 'Miku Push-win32-x64.zip' 'Miku Push-win32-x64'
	cd ../..
	docker run --rm -i -v "$$PWD:/work" amake/innosetup app/installer/windows/installer-x64.iss
	docker run --rm -i -v "$$PWD:/work" amake/innosetup app/installer/windows/installer-arm64.iss

build-app-linux: app/dist-electron
	cd app
	npm run package:linux
	cd target
	[[ -f 'Miku Push-linux-arm64.zip' ]] && rm 'Miku Push-linux-arm64.zip'
	[[ -f 'Miku Push-linux-x64.zip' ]] && rm 'Miku Push-linux-x64.zip'
	zip -r 'Miku Push-linux-arm64.zip' 'Miku Push-linux-arm64'
	zip -r 'Miku Push-linux-x64.zip' 'Miku Push-linux-x64'

build: \
	build-server \
	build-app-macos \
	build-app-windows \
	build-app-linux
