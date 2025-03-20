import { Upload } from "./upload"
import { UploadProgress } from "./upload-progress"

export class UploadRequest {
    #file
    #upload
    #progress

    /**
     * Constructor
     * @param {object} param0
     * @param {File} param0.file
     * @param {Upload} param0.upload
     * @param {UploadProgress} param0.progress
     */
    constructor({ file, upload, progress }) {
        this.#file = file
        this.#upload = upload
        this.#progress = progress
    }

    get file() {
        return this.#file
    }

    get id() {
        return this.#upload.id
    }

    get name() {
        return this.#upload.name
    }

    get mimeType() {
        return this.#upload.mimeType
    }

    get upload() {
        return this.#upload
    }

    get progress() {
        return this.#progress.progress
    }

    get speed() {
        return this.#progress.speed
    }

    get error() {
        return this.#progress.error
    }

    get finishedSuccess() {
        return this.#progress.finishedSuccess
    }

    get finishedFailed() {
        return this.#progress.finishedFailed
    }

    updateProgress(newValue) {
        const updated = this.#progress.update(newValue)
        return new UploadRequest({ ...this.#toPlainObject(), progress: updated })
    }

    finishWithError(error) {
        return new UploadRequest({ 
            ...this.#toPlainObject(), 
            progress: this.#progress.finishWithError(error) 
        })
    }

    finishSuccess() {
        return new UploadRequest({ 
            ...this.#toPlainObject(), 
            progress: this.#progress.finishSuccess() 
        })
    }

    #toPlainObject() {
        return {
            file: this.#file,
            upload: this.#upload,
            progress: this.#progress,

        }
    }

    static async createFromFile(file) {
        return new UploadRequest({
            file,
            upload: await Upload.fromFile(file),
            progress: UploadProgress.create()
        })
    }
}