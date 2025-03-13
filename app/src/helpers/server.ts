import axios from 'axios'
import { FileRequest } from "../model/file-request";

export async function upload (request: FileRequest) {
  await axios.post('http://localhost:8080/upload', request.bytes.buffer, {
    headers: {
      'X-File-Id': request.id,
      'X-File-Name': request.name,
      'Content-Type': request.mimeType,
    },
    onUploadProgress: (event) => {
      if (event.total) {
        const percent = (event.loaded / event.total) * 100
        //const speed = progressEvent.loaded / elapsedTime // Bytes por segundo

        console.log(`Progreso: ${percent.toFixed(2)}%`)
      }
    },
  })
}
