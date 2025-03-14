import axios, { AxiosProgressEvent } from 'axios'
import { UploadProgress } from '../model/upload-progress'
import { BehaviorSubject } from 'rxjs'

interface UploadRequest {
  file: File
  subject: BehaviorSubject<UploadProgress>
}

const uploadQueue: UploadRequest[] = []
let isConsumingUploadQueue = false

export async function requestUploadForFile (file: File): Promise<BehaviorSubject<UploadProgress>> {
  const subject = new BehaviorSubject<UploadProgress>(await UploadProgress.fromFile(file))
  uploadQueue.push({ file, subject })
  startConsumeUploadQueue()

  return subject
}

async function startConsumeUploadQueue () {
  if (isConsumingUploadQueue) return
  isConsumingUploadQueue = true

  while (uploadQueue.length > 0) {
    const request = uploadQueue.shift()
    await upload(request)
  }

  isConsumingUploadQueue = false
}

async function upload (request: UploadRequest) {
  const file = request.file
  const subject = request.subject
  const progress = subject.getValue()
  const details = progress.details

  const headers = {
    'X-File-Id': details.id,
    'X-File-Name': details.name,
    'Content-Type': details.mimeType,
  }

  const onUploadProgress = (event: AxiosProgressEvent) => {
    if (event.total) {
      // const speed = progressEvent.loaded / elapsedTime // Bytes por segundo
      progress.updateProgress(
        (event.loaded / event.total),
        0
      )

      subject.next(progress)
      console.log(`Progreso: ${progress.progress.toFixed(2)}`)
    }
  }

  try {
    const response = await axios.post('http://localhost:8080/upload', file, {
      headers,
      onUploadProgress,
    })

    if (response.status !== 201) {
      progress.finishWithError(`Error uploading file: ${response.statusText}`)
    } else {
      progress.finishSuccessful()
    }
  } catch (exception) {
    progress.finishWithError(exception.message)
  }

  subject.next(progress)
}
