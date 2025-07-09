import { app } from 'electron'
import { database } from './helpers/database.ts'
import { MainWindow } from './main-window.ts'
import { SystemChannelsBinder } from './ipc/system-binder.ts'
import { UploadChannelsBinder } from './ipc/upload-binder.ts'
import { setupTray } from './tray.ts'
import { appDataDirectory } from './helpers/file-system.ts'
import fs from 'fs'

export class Application {
	private readonly mainWindow: MainWindow

	constructor() {
		this.mainWindow = new MainWindow()
	}

	public onAppReady() {
		this.setupAppDataDirectory()

		this.mainWindow.initialize()
		this.setupIPCChannels()
		database.sync()

		setupTray(this.mainWindow)
	}

	public onAppSecondInstance() {
		if (this.mainWindow.isMinimized()) {
			this.mainWindow.restore()
		}

		this.mainWindow.focus()
	}

	public onAppQuit() {
		database.close()
	}

	private setupAppDataDirectory() {
		const directory = appDataDirectory()

		if (!fs.existsSync(directory)) {
			fs.mkdirSync(directory, { recursive: true })
		}
	}

	private setupIPCChannels() {
		SystemChannelsBinder.bind()
		UploadChannelsBinder.bind(this.mainWindow)
	}

	public static run() {
		console.log('Bundle ID:', app.getName())
		console.log('App Path:', app.getAppPath())
		console.log('App Version:', app.getVersion())

		const isLockObtained = app.requestSingleInstanceLock()

		if (!isLockObtained) {
			console.log('app is already running, closing second instance')
			app.quit()
			return
		}

		app.whenReady().then(() => {
			const application = new Application()
			application.onAppReady()
			this.setupEventListeners(application)
		})
	}

	private static setupEventListeners(application: Application) {
		app.on('quit', () => application.onAppQuit())
		// TODO: catch from contextual menu path to upload
		// https://www.electronjs.org/docs/latest/api/app#apprequestsingleinstancelockadditionaldata
		app.on('second-instance', () => application.onAppSecondInstance())
	}
}
