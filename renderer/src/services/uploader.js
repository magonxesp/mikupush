import { create } from "../http/create"
import { upload } from "../http/upload"
import { createUpload } from "../ipc/upload"
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
            const item = this.#queue.shift()

            if (typeof item === 'undefined') {
                continue
            }

            const [request, onProgressUpdateCallback] = item
            const state = new UploadRequestState(request, onProgressUpdateCallback)

            try {
                await create(request)
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

        this.#isProcessingQueue = false
    }
}

/**
 * @typedef {(request: UploadRequest) => void} OnProgressCallback
 */

class UploadRequestState {
  #request;
  #notifyProgress;

  /**
   * Constructor
   * @param {UploadRequest} request
   * @param {OnProgressCallback} notifyProgress
   */
  constructor(request, notifyProgress) {
    this.#request = request;
    this.#notifyProgress = notifyProgress;
  }

  /**
   * Update request state
   * @param {(state: UploadRequest) => UploadRequest} updater
   */
  update(updater) {
    console.log('updating upload state')
    this.#request = updater(this.#request);
    this.#notifyProgress(this.#request);
  }
}
