export class UploadProgress {
    #progress
    #speed
    #error
    #finished

    constructor({ progress, speed, error, finished }) {
        this.#progress = progress
        this.#speed = speed
        this.#error = error
        this.#finished = finished
    }

    get progress() {
        return this.#progress
    }

    get speed() {
        return this.#speed
    }

    get error() {
        return this.#error
    }

    get finished() {
        return this.#finished
    }

    get finishedSuccess() {
        return this.#finished && this.#error === ''
    }

    get finishedFailed() {
        return this.#finished && this.#error !== ''
    }

    update(newValues) {
        return new UploadProgress({ ...this.#toPlainObject(), ...newValues })
    }

    finishWithError(error) {
        return new UploadProgress({ ...this.#toPlainObject(), finished: true, error })
    }

    finishSuccess() {
        return new UploadProgress({ ...this.#toPlainObject(), finished: true, error: '' })
    }

    #toPlainObject() {
        return {
            progress: this.#progress,
            speed: this.#speed,
            error: this.#error,
            finished: this.#finished
        }
    }

    static create() {
        return new UploadProgress({
            progress: 0,
            speed: 0,
            error: '',
            finished: false
        })
    }
}