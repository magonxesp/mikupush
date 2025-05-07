import { app, BrowserWindow, nativeImage } from 'electron'
import fs from 'fs'
import { appDataDirectory } from './helpers/file-system.ts'
import { database } from './helpers/database.ts'
import path from 'path'
import { setupTray } from './tray'
import started from 'electron-squirrel-startup'
import { UploadChannelsBinder } from './ipc/upload-binder.ts'
import { SystemChannelsBinder } from './ipc/system-binder.ts'
import { appEnv } from '../shared/helpers/environment.ts'

const isDevMode = appEnv() === 'dev'
const isPreviewMode = appEnv() === 'preview'

if (started) {
	app.quit()
}

if (isDevMode) {
	console.info('dev mode enabled')
}

let isAppQuitting = false

function createWindow() {
	const window = new BrowserWindow({
		width: 800,
		height: 600,
		frame: false, // Oculta la barra de título
		titleBarStyle: 'hidden',
		titleBarOverlay: {
			color: 'rgba(0, 0, 0, 0)',
			symbolColor: '#ffffff',
			height: 32
		},
		title: 'Miku Push!',
		icon: nativeImage.createFromPath(path.join(__dirname, 'assets/icon.png')),
		webPreferences: {
			preload: path.join(__dirname, 'preload.js'),
			devTools: isDevMode || isPreviewMode,
		}
	})

	if (process.env.VITE_DEV_SERVER_URL) {
		console.log('using vite dev server')
		window.webContents.openDevTools()
		window.loadURL(process.env.VITE_DEV_SERVER_URL)
	} else {
		window.loadFile(path.join(__dirname, '../dist/index.html'))
	}

	window.on('close', function (evt) {
		if (!isAppQuitting) {
			evt.preventDefault()
			window.hide()
		}
	})

	database.sync()
	return window
}

function ensureAppDataDirectoryIsCreated() {
	const directory = appDataDirectory()

	if (!fs.existsSync(directory)) {
		fs.mkdirSync(directory, { recursive: true })
	}
}

app.whenReady().then(() => {
	app.setAppUserModelId('io.mikupush.MikuPush')
	ensureAppDataDirectoryIsCreated()

	const window = createWindow()

	SystemChannelsBinder.bind()
	UploadChannelsBinder.bind(window)

	setupTray(window)
})

app.on('quit', () => {
	database.close()
})

app.on('before-quit', function () {
	isAppQuitting = true
})
