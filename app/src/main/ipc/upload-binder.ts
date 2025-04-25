import { BrowserWindow, ipcMain } from 'electron'
import { Uploader } from '../../shared/services/uploader.ts'
import { uploader, uploadRepository } from '../service-container.ts'
import { UploadRequest } from '../../shared/model/upload-request.ts'
import { UploadRepository } from '../../shared/repository/upload-repository.ts'
import {
	uploadEnqueueChannel,
	uploadFindAllChannel,
	uploadOnProgressChannel,
	uploadRetryChannel
} from '../../shared/ipc/upload-channels.ts'
import { fileDetails } from '../helpers/file-system.ts'

export class UploadChannelsBinder {
	private readonly uploader: Uploader
	private readonly uploadRepository: UploadRepository
	private readonly window: BrowserWindow

	constructor (window: BrowserWindow, uploadRepository: UploadRepository, uploader: Uploader) {
		this.window = window
		this.uploadRepository = uploadRepository
		this.uploader = uploader
	}

	public async enqueue(filePath: string) {
		const details = fileDetails(filePath)

		return await this.uploader.enqueue(details, (request) => {
			this.window.webContents.send(uploadOnProgressChannel, request)
		})
	}

	public retry(request: UploadRequest) {
		this.uploader.retry(request, (request) => {
			this.window.webContents.send(uploadOnProgressChannel, request)
		})
	}

	public async findAll() {
		return await this.uploadRepository.findAll()
	}

	static bind(window: BrowserWindow) {
		const uploadIpc = new UploadChannelsBinder(window, uploadRepository, uploader)

		ipcMain.handle(uploadEnqueueChannel, async (_, filePath: string) => {
			return await uploadIpc.enqueue(filePath)
		})

		ipcMain.on(uploadRetryChannel, async (_, request: UploadRequest) => {
			uploadIpc.retry(request)
		})

		ipcMain.handle(uploadFindAllChannel, async () => {
			return await uploadIpc.findAll()
		})
	}
}


