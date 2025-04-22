import { Upload } from './upload'
import { UploadProgress, UploadProgressObject } from './upload-progress'

export interface UploadRequestObject {
    file: File
    upload: Upload
    progress: UploadProgress
    controller?: AbortController
    retry?: boolean
}

export class UploadRequest {
	public readonly file: File
	public readonly upload: Upload
	private readonly _progress: UploadProgress
	private _controller: AbortController
	private _retry: boolean

	constructor({ file, upload, progress, controller }: UploadRequestObject) {
		this.file = file
		this.upload = upload
		this._controller = controller ?? new AbortController()
		this._progress = progress
		this._retry = false
	}

	get id() {
		return this.upload.id
	}

	get name() {
		return this.upload.name
	}

	get mimeType() {
		return this.upload.mimeType
	}

	get progress() {
		return this._progress.progress
	}

	get speed() {
		return this._progress.speed
	}

	get error() {
		return this._progress.error
	}

	get isInProgress() {
		return this._progress.isInProgress
	}

	get finishedSuccess() {
		return this._progress.finishedSuccess
	}

	get finishedFailed() {
		return this._progress.finishedFailed
	}

	get controller() {
		return this._controller
	}

	get isRetried() {
		return this._retry
	}

	updateProgress(newValue: Partial<UploadProgressObject>) {
		const updated = this._progress.update(newValue)
		return new UploadRequest({ ...this.#toPlainObject(), progress: updated })
	}

	finishWithError(error: Error | string | unknown) {
		return new UploadRequest({ 
			...this.#toPlainObject(), 
			progress: this._progress.finishWithError(error)
		})
	}

	finishSuccess() {
		return new UploadRequest({ 
			...this.#toPlainObject(), 
			progress: this._progress.finishSuccess()
		})
	}

	abort() {
		if (this.controller != null) {
			this.controller.abort()
		}
	}

	retry() {
		this._controller = new AbortController()
		this._retry = true
	}

	#toPlainObject(): UploadRequestObject {
		return {
			file: this.file,
			upload: this.upload,
			progress: this._progress,
			controller: this.controller,
			retry: this._retry,
		}
	}

	static async createFromFile(file: File) {
		return new UploadRequest({
			file,
			upload: await Upload.fromFile(file),
			progress: UploadProgress.create()
		})
	}
}
