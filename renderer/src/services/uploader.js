import { upload } from "../http/upload"
import { UploadRequest } from "../model/upload-request"

export class Uploader {
    /**
     * @typedef {(request: UploadRequest) => void} OnProgressUpdateCallback
     */

    /**
     * @type {[UploadRequest, OnProgressUpdateCallback][]}
     */
    #queue = []
    #isProcessingQueue = false

    /**
     * Add file to upload queue
     * @param {File} file
     * @param {OnProgressUpdateCallback} onProgressUpdate
     * @returns {Promise<UploadRequest>}
     */
    async enqueue(file, onProgressUpdate = () => {}) {
        const request = await UploadRequest.createFromFile(file)
        this.#queue.push([request, onProgressUpdate])

        this.#startProcesingQueue()
        return request
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
            const [request, onProgressUpdateCallback] = this.#queue.shift()
            await upload(request, onProgressUpdateCallback)
        }

        this.#isProcessingQueue = false
    }
}