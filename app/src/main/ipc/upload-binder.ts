import { BrowserWindow, ipcMain, IpcMainEvent, IpcMainInvokeEvent } from 'electron'
import { Uploader } from '../../shared/services/uploader.ts'
import { uploader, uploadRepository } from '../service-container.ts'
import { SerializableUploadRequest, UploadRequest } from '../../shared/model/upload-request.ts'
import { UploadRepository } from '../../shared/repository/upload-repository.ts'
import {
	uploadAbortChannel,
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

	public async enqueue(_: IpcMainInvokeEvent, filePaths: string[]): Promise<SerializableUploadRequest[]> {
		const details = filePaths.map(fileDetails)

		const progressCallback = (request: SerializableUploadRequest) => {
			this.window.webContents.send(uploadOnProgressChannel, request)
		}

		let requests: UploadRequest[] = []

		if (details.length === 1) {
			requests = [await this.uploader.enqueue(details[0], progressCallback)]
		} else if (details.length > 1) {
			requests = await this.uploader.enqueueMany(details, progressCallback)
		}

		return requests.map(request => request.toSerializable())
	}

	public retry(_: IpcMainEvent, serializable: SerializableUploadRequest) {
		console.log('retrying upload', serializable)
		const request = UploadRequest.fromSerializable(serializable)

		this.uploader.retry(request, (request) => {
			this.window.webContents.send(uploadOnProgressChannel, request)
		})
	}

	public abort(_: IpcMainEvent, uploadId: string) {
		console.log('aborting upload', uploadId)
		this.uploader.abort(uploadId)
	}

	public async findAll() {
		return this.uploadRepository.findAll()
			.then(uploads => uploads.map(upload => upload.toSerializable()))
	}

	static bind(window: BrowserWindow) {
		const uploadIpc = new UploadChannelsBinder(window, uploadRepository, uploader)

		ipcMain.handle(uploadEnqueueChannel, (event, filePaths) => uploadIpc.enqueue(event, filePaths))
		ipcMain.on(uploadRetryChannel, (event, serializable) => uploadIpc.retry(event, serializable))
		ipcMain.on(uploadAbortChannel, (event, uploadId) => uploadIpc.abort(event, uploadId))
		ipcMain.handle(uploadFindAllChannel, () =>  uploadIpc.findAll())
	}
}


