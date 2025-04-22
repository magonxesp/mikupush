export interface UploadProgressObject {
    progress: number
    speed: number
    error: string
    finished: boolean
}

export class UploadProgress {
    public readonly progress: number
    public readonly speed: number
    public readonly error: string
    public readonly finished: boolean

    constructor({ progress, speed, error, finished }: UploadProgressObject) {
        this.progress = progress
        this.speed = speed
        this.error = error
        this.finished = finished
    }

    get isInProgress() {
        return !this.finished && this.progress < 1
    }

    get finishedSuccess() {
        return this.finished && this.error === ''
    }

    get finishedFailed() {
        return this.finished && this.error !== ''
    }

    update(newValues: Partial<UploadProgressObject>) {
        return new UploadProgress({ ...this.toPlainObject(), ...newValues })
    }

    finishWithError(error: Error | string | unknown) {
        let errorMessage = 'an unknown error occurred during upload'

        if (error instanceof Error) {
            errorMessage = error.message
        } else if (typeof error === 'string') {
            errorMessage = error
        }

        return new UploadProgress({ ...this.toPlainObject(), finished: true, error: errorMessage })
    }

    finishSuccess() {
        return new UploadProgress({ ...this.toPlainObject(), finished: true, error: '' })
    }

    toPlainObject(): UploadProgressObject {
        return {
            progress: this.progress,
            speed: this.speed,
            error: this.error,
            finished: this.finished
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