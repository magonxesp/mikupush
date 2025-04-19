.ONESHELL:
SHELL := /bin/bash 

.PHONY: build-server build-app build

build-server:
	cd server
	GOOS=windows GOARCH=arm64 go build -o target/server-windows-arm64.exe
	GOOS=windows GOARCH=amd64 go build -o target/server-windows-amd64.exe
	GOOS=darwin GOARCH=arm64 go build -o target/server-darwin-arm64
	GOOS=darwin GOARCH=amd64 go build -o target/server-darwin-amd64
	GOOS=linux GOARCH=arm64 go build -o target/server-linux-arm64
	GOOS=linux GOARCH=amd64 go build -o target/server-linux-amd64

build-app:
	cd app
	if [[ ! -d node_modules ]]; then npm install; fi
	npm run build:prod
	npm run package
	cd target
	zip -r 'Miku Push-darwin-arm64.zip' 'Miku Push-darwin-arm64'
	zip -r 'Miku Push-darwin-amd64.zip' 'Miku Push-darwin-amd64'
	zip -r 'Miku Push-win32-arm64.zip' 'Miku Push-win32-arm64'
	zip -r 'Miku Push-win32-amd64.zip' 'Miku Push-win32-amd64'
	zip -r 'Miku Push-linux-arm64.zip' 'Miku Push-linux-arm64'
	zip -r 'Miku Push-linux-amd64.zip' 'Miku Push-linux-amd64'

build: build-server build-app
