import { FileDetails } from "./file-details"

export class UploadProgress {
  readonly details: FileDetails
  private _progress: number = 0
  private _speed: number = 0
  private finished: boolean = false
  private error: string | null = null
  private observers: ((progress: UploadProgress) => void)[] = []

  constructor(details: FileDetails) {
    this.details = details
  }

  get progress() {
    return this._progress
  }

  get speed() {
    return this._speed
  }

  get isFinishedSuccess() {
    return this.finished && this.error == null
  }

  get isFinishedFailed() {
    return this.finished && this.error != null
  }

  get isInProgress() {
    return !this.finished
  }

  finishWithError(error: string) {
    this.finished = true
    this.error = error

    this.notifyAll()
  }

  finishSuccessful() {
    this.finished = true
    this.error = null

    this.notifyAll()
  }

  onUpdate(observer: (progress: UploadProgress) => void) {
    this.observers.push(observer)
  }

  updateProgress(progress: number, speed: number) {
    this._progress = progress
    this._speed = speed

    this.notifyAll()
  }

  private notifyAll() {
    this.observers.forEach(observer => observer(this))
  }

  static async fromFile (file: File) {
    return new UploadProgress(await FileDetails.fromFile(file))
  }
}
