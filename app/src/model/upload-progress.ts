import { FileDetails } from './file-details'

export class UploadProgress {
  readonly details: FileDetails
  readonly file: File
  private _progress: number = 0
  private _speed: number = 0
  private _error: string | null = null
  private finished: boolean = false

  constructor (details: FileDetails, file: File) {
    this.details = details
    this.file = file
  }

  get progress () {
    return this._progress
  }

  get speed () {
    return this._speed
  }

  get error () {
    return this._error
  }

  get isFinishedSuccess () {
    return this.finished && this.error == null
  }

  get isFinishedFailed () {
    return this.finished && this._error != null
  }

  get isInProgress () {
    return !this.finished
  }

  finishWithError (error: string) {
    console.log('upload error')
    this.finished = true
    this._error = error
  }

  finishSuccessful () {
    this.finished = true
    this._error = null
  }

  updateProgress (progress: number, speed: number) {
    this._progress = progress
    this._speed = speed
  }

  static async fromFile (file: File) {
    const details = await FileDetails.fromFile(file)
    return new UploadProgress(details, file)
  }
}
