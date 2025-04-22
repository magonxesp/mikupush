import { create } from '../http/create'
import { upload } from '../http/upload'
import { createUpload } from '../ipc/upload'
import { UploadRequest } from '../model/upload-request'

type OnProgressUpdateCallback = (request: UploadRequest) => void
type QueueItem = [UploadRequest, OnProgressUpdateCallback]

export class Uploader {
	private queue: QueueItem[] = []
	private isProcessingQueue = false

	async enqueue(file: File, onProgressUpdate: OnProgressUpdateCallback = () => {}) {
		const request = await UploadRequest.createFromFile(file)
		this.queue.push([request, onProgressUpdate])

		this.startProcesingQueue()
		return request
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

				await createUpload(request.upload)
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
