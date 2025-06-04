.ONESHELL:
SHELL := bash
.SHELLFLAGS := -e -o pipefail -c

.PHONY: \
	build-server \
	build-app-macos \
	build-app-windows \
	build-app-linux \
	build \
	clean

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
	npm run make -- --platform=darwin

build-app-windows: app/dist-electron
	cd app
	npm run make -- --platform=win32

build-app-linux: app/dist-electron
	cd app
	npm run make -- --platform=linux

build: \
	build-server \
	build-app-macos \
	build-app-windows \
	build-app-linux

clean:
	rm -r app/dist
	rm -r app/dist-electron
	rm -r app/target
	rm -r server/target
