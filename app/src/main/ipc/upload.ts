import { BrowserWindow, ipcMain } from 'electron'
import { Uploader } from '../../shared/services/uploader.ts'
import { fileDetails } from '../helpers/file.ts'
import { uploader } from '../service-container.ts'

export class UploadIpc {
	private readonly uploader: Uploader
	private readonly window: BrowserWindow

	constructor (window: BrowserWindow, uploader: Uploader) {
		this.window = window
		this.uploader = uploader
	}

	public enqueue(filePath: string) {
		const details = fileDetails(filePath)

		this.uploader.enqueue(details, (request) => {
			this.window.webContents.send('upload-progress', request.toPlainObject())
		}).then(request => {
			this.window.webContents.send('upload-enqueued', request.toPlainObject())
		})
	}

	static listenIpcEvents(window: BrowserWindow) {
		const uploadIpc = new UploadIpc(window, uploader)

		ipcMain.on('upload-equeue', (_, filePath: string) => uploadIpc.enqueue(filePath))
	}
}


