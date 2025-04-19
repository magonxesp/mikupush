.ONESHELL:
SHELL := bash
.SHELLFLAGS := -e -o pipefail -c

.PHONY: \
	build-server \
	build-app \
	build-app-windows-installer \
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

build-app:
	cd app
	[[ -d target ]] && rm -r target
	[[ ! -d node_modules ]] && npm install
	npm run build:prod
	npm run package
	cd target
	zip -r 'Miku Push-darwin-arm64.zip' 'Miku Push-darwin-arm64'
	zip -r 'Miku Push-darwin-x64.zip' 'Miku Push-darwin-x64'
	zip -r 'Miku Push-win32-arm64.zip' 'Miku Push-win32-arm64'
	zip -r 'Miku Push-win32-x64.zip' 'Miku Push-win32-x64'
	zip -r 'Miku Push-linux-arm64.zip' 'Miku Push-linux-arm64'
	zip -r 'Miku Push-linux-x64.zip' 'Miku Push-linux-x64'

build-app-windows-installer:
	docker run --rm -i -v "$$PWD:/work" amake/innosetup app/installer/windows/installer-x64.iss
	docker run --rm -i -v "$$PWD:/work" amake/innosetup app/installer/windows/installer-arm64.iss

build: \
	build-server \
	build-app \
	build-app-windows-installer
