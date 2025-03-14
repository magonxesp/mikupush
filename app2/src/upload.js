import axios from 'axios'
import { fileTypeFromBlob } from 'file-type'
import { v4 as uuidv4 } from 'uuid'

const uploadQueue = []
let isConsumingUploadQueue = false

export async function addToUploadQueue(file, onUpdate) {
  const upload = {
    file,
    id: uuidv4(),
    name: file.name,
    size: file.size,
    mimeType: file.type ?? await fileTypeFromBlob(file),
    progress: 0,
    error: '',
    finished: false,
    speed: 0
  }

  uploadQueue.push(upload)
  startConsumeUploadQueue(onUpdate)

  return upload
}

async function startConsumeUploadQueue(onUpdate) {
  if (isConsumingUploadQueue) return
  isConsumingUploadQueue = true

  while (uploadQueue.length > 0) {
    const upload = uploadQueue.shift()
    await performUpload(upload, onUpdate)
  }

  isConsumingUploadQueue = false
}

async function performUpload (upload, onUpdate) {
  if (upload.mimeType == null) {
    upload.error = 'Unknown file type'
    upload.finished = true
    onUpdate(upload)
    return
  }

  const headers = {
    'X-File-Id': upload.id,
    'X-File-Name': upload.name,
    'Content-Type': upload.mimeType,
  }

  const onUploadProgress = (event) => {
    if (event.total) {
      // const speed = progressEvent.loaded / elapsedTime // Bytes por segundo
      upload.progress = event.loaded / event.total
      upload.speed = 0
      onUpdate(upload)
    }
  }

  try {
    const response = await axios.post('http://localhost:8080/upload', upload.file, {
      headers,
      onUploadProgress,
    })

    if (response.status !== 201) {
      upload.error = `Error uploading file: ${response.statusText}`
    } else {
      upload.error = ''
    }
  } catch (exception) {
    if (exception instanceof Error) {
      upload.error = exception.message
    } else {
      upload.error = 'An unknown error occurred during file upload'
    }
  }

  upload.finished = true
  onUpdate(upload)
}
