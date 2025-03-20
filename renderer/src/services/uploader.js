import { upload } from "../http/upload"
import { UploadRequest } from "../model/upload-request"

export class Uploader {
    /**
     * @type {UploadRequest[]}
     */
    #queue = []
    #isProcessingQueue = false
    /**
     * @type {((request: UploadRequest) => void)[]}
     */
    #progressListeners = []

    /**
     * Add file to upload queue
     * @param {File} file
     * @returns {Promise<UploadRequest>}
     */
    async enqueue(file) {
        const request = await UploadRequest.createFromFile(file)
        this.#queue.push(request)

        this.#startProcesingQueue()
        return request
    }

    /**
     * Add progress listener
     * @param {(request: UploadRequest) => void} listener 
     */
    addProgressListener(listener) {
        this.#progressListeners.push(listener)
    }

    #startProcesingQueue() {
        if (this.#isProcessingQueue) {
            return
        }

        this.#isProcessingQueue = true
        this.#processQueue()
    }

    async #processQueue() {
        while (this.#queue.length > 0) {
            const request = this.#queue.shift()
            await upload(request, (updated) => this.#notifyProgress(updated))
        }

        this.#isProcessingQueue = false
    }

    /**
     * Notify all listeners to request updates
     * @param {UploadRequest} request 
     */
    #notifyProgress(request) {
        this.#progressListeners.forEach(listener => listener(request))
    }
}