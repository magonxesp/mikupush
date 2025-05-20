import { create } from '../http/create.ts'
import { upload } from '../http/upload.ts'
import { SerializableUploadRequest, UploadRequest } from '../model/upload-request'
import { FileDetails } from '../model/file-details.ts'
import { UploadRepository } from '../repository/upload-repository.ts'
import { Notifier } from './notifier.ts'
import fs from 'fs'
import { CanceledError } from 'axios'

type OnProgressUpdateCallback = (request: SerializableUploadRequest) => void
type QueueItem = [UploadRequest, OnProgressUpdateCallback]

export class Uploader {
	private queue: QueueItem[] = []
	private inProgressUploads: UploadRequest[] = []
	private isProcessingQueue = false
	private readonly uploadRepository: UploadRepository
	private readonly notifier: Notifier

	constructor(uploadRepository: UploadRepository, notifier: Notifier) {
		this.uploadRepository = uploadRepository
		this.notifier = notifier
	}

	public async enqueue(fileDetails: FileDetails, onProgressUpdate: OnProgressUpdateCallback = () => {}) {
		const request = await UploadRequest.fromFileDetails(fileDetails)
		this.queue.push([request, onProgressUpdate])

		this.notifier.showNotification({
			title: `Uploading file ${request.name} 🚀`,
			body: `The file ${request.name} is added to the upload queue`
		})

		this.startProcesingQueue()
		return request
	}

	public async enqueueMany(fileDetails: FileDetails[], onProgressUpdate: OnProgressUpdateCallback = () => {}) {
		const requests = await Promise.all(fileDetails.map((details) => UploadRequest.fromFileDetails(details)))
		this.queue.push(...requests.map((request) => [request, onProgressUpdate] as QueueItem))

		this.notifier.showNotification({
			title: `Uploading ${fileDetails.length} files 🚀`,
			body: 'The files are added to the upload queue'
		})

		this.startProcesingQueue()
		return requests
	}

	public retry(request: UploadRequest, onProgressUpdate: OnProgressUpdateCallback = () => {}) {
		request.retry()

		this.queue.push([request, onProgressUpdate])
		this.startProcesingQueue()
	}

	public abort(uploadId: string) {
		const request = this.inProgressUploads
			.filter(request => request.id === uploadId)
			.pop()

		if (request != null) {
			request.abort()
			this.removeFromInProgressUploads(request)
		}
	}

	private startProcesingQueue() {
		if (this.isProcessingQueue) {
			return
		}

		this.isProcessingQueue = true
		this.processQueue()
	}

	private async processQueue() {
		console.log('processing upload queue')

		while (this.queue.length > 0) {
			const item = this.queue.shift()

			if (typeof item === 'undefined') {
				continue
			}

			await this.uploadItem(item)
		}

		this.isProcessingQueue = false
	}

	private async uploadItem(item: QueueItem) {
		const [request, onProgressUpdateCallback] = item
		console.log('uploading file', request.id)

		this.inProgressUploads.push(request)

		try {
			await this.postUpload(request)
			await upload(request, fs.createReadStream(request.file.path), (progress) => {
				request.updateProgress(progress.progress, progress.speed)
				onProgressUpdateCallback(request.toSerializable())
			})

			await this.uploadRepository.save(request.upload)
			request.finishSuccess()
		} catch (error) {
			if (error instanceof CanceledError) {
				console.log('upload canceled')
			} else {
				console.log('upload error', error)
				request.finishWithError(error)
			}
		}

		onProgressUpdateCallback(request.toSerializable())
		this.removeFromInProgressUploads(request)
		console.log('uploading file finished', request.id)
	}

	private async postUpload(request: UploadRequest) {
		if (request.isRetried) {
			try {
				await create(request)
			} catch (exception) {
				console.warn('(retried request) failed create file on server', exception)
			}
		} else {
			await create(request)
		}
	}

	private removeFromInProgressUploads(request: UploadRequest) {
		this.inProgressUploads = this.inProgressUploads.filter(inProgressRequest => inProgressRequest.id !== request.id)
	}
}
