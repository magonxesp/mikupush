import axios from 'axios'
import { UploadProgress } from '../model/upload-progress'

interface UploadRequest {
  file: File
  progress: UploadProgress
}

const uploadQueue: UploadRequest[] = []
let isConsumingUploadQueue = false

export async function requestUploadForFile (file: File) {
  const progress = await UploadProgress.fromFile(file)
  uploadQueue.push({ file, progress })
  startConsumeUploadQueue()

  return progress
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
  const progress = request.progress
  const details = progress.details

  await axios.post('http://localhost:8080/upload', file, {
    headers: {
      'X-File-Id': details.id,
      'X-File-Name': details.name,
      'Content-Type': details.mimeType,
    },
    onUploadProgress: (event) => {
      if (event.total) {
        // const speed = progressEvent.loaded / elapsedTime // Bytes por segundo
        progress.updateProgress(
          (event.loaded / event.total),
          0
        )

        console.log(`Progreso: ${progress.progress.toFixed(2)}`)
      }
    },
  })
}
