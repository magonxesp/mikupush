import { app } from 'electron'
import { database } from './helpers/database.ts'
import { MainWindow } from './main-window.ts'
import { SystemChannelsBinder } from './ipc/system-binder.ts'
import { UploadChannelsBinder } from './ipc/upload-binder.ts'
import { setupTray } from './tray.ts'
import { appDataDirectory } from './helpers/file-system.ts'
import fs from 'fs'

export class Application {
	private mainWindow: MainWindow

	constructor() {
		this.mainWindow = new MainWindow()
	}

	public onAppReady() {
		this.setupAppDataDirectory()
		app.setAppUserModelId('io.mikupush.client')

		this.mainWindow.initialize()
		this.setupIPCChannels()
		database.sync()

		setupTray(this.mainWindow)
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
		app.whenReady().then(() => {
			const application = new Application()
			application.onAppReady()
			this.setupEventListeners(application)
		})
	}

	private static setupEventListeners(application: Application) {
		app.on('quit', () => application.onAppQuit())
	}
}
