import { create } from '../http/create.ts'
import { upload } from '../http/upload.ts'
import { UploadRequest } from '../model/upload-request'
import { FileDetails } from '../model/file-details.ts'
import { UploadRepository } from '../repository/upload-repository.ts'
import { Notifier } from './notifier.ts'

type OnProgressUpdateCallback = (request: UploadRequest) => void
type QueueItem = [UploadRequest, OnProgressUpdateCallback]

export class Uploader {
	private queue: QueueItem[] = []
	private isProcessingQueue = false
	private readonly uploadRepository: UploadRepository
	private readonly notifier: Notifier

	constructor(uploadRepository: UploadRepository, notifier: Notifier) {
		this.uploadRepository = uploadRepository
		this.notifier = notifier
	}

	async enqueue(fileDetails: FileDetails, onProgressUpdate: OnProgressUpdateCallback = () => {}) {
		const request = await UploadRequest.fromFileDetails(fileDetails)
		this.queue.push([request, onProgressUpdate])

		this.notifier.showNotification({
			title: `Uploading file ${request.name} 🚀`,
			body: `The file ${request.name} is added to the upload queue`
		})

		this.startProcesingQueue()
		return request
	}

	async enqueueMany(fileDetails: FileDetails[], onProgressUpdate: OnProgressUpdateCallback = () => {}) {
		const requests = await Promise.all(fileDetails.map((details) => UploadRequest.fromFileDetails(details)))
		this.queue.push(...requests.map((request) => [request, onProgressUpdate] as QueueItem))

		this.notifier.showNotification({
			title: `Uploading ${fileDetails.length} files 🚀`,
			body: 'The files are added to the upload queue'
		})

		this.startProcesingQueue()
		return requests
	}

	retry(request: UploadRequest, onProgressUpdate: OnProgressUpdateCallback = () => {}) {
		request.retry()

		this.queue.push([request, onProgressUpdate])
		this.startProcesingQueue()
	}

	private startProcesingQueue() {
		if (this.isProcessingQueue) {
			return
		}

		this.isProcessingQueue = true
		this.processQueue()
	}

	private async processQueue() {
		while (this.queue.length > 0) {
			const item = this.queue.shift()

			if (typeof item === 'undefined') {
				continue
			}

			const [request, onProgressUpdateCallback] = item
			const state = new UploadRequestState(request, onProgressUpdateCallback)

			try {
				if (request.isRetried) {
					try {
						await create(request)
					} catch (exception) {
						console.warn('(retried request) failed create file on server', exception)
					}
				} else {
					await create(request)
				}

				await upload(request, (progress) => {
					state.update(previous => previous.updateProgress(progress))
				})

				await this.uploadRepository.save(request.upload)
				state.update(previous => previous.finishSuccess())
			} catch (error) {
				console.log('upload error', error)
				state.update(previous => previous.finishWithError(error))
			}
		}

		this.isProcessingQueue = false
	}
}

type OnProgressCallback = (request: UploadRequest) => void

class UploadRequestState {
	private request: UploadRequest
	private notifyProgress: OnProgressCallback

	constructor(request: UploadRequest, notifyProgress: OnProgressCallback) {
		this.request = request
		this.notifyProgress = notifyProgress
	}

	update(updater: (state: UploadRequest) => UploadRequest) {
		this.request = updater(this.request)
		this.notifyProgress(this.request)
	}
}
