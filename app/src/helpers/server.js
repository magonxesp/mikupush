import axios from 'axios'

/**
 * Upload file
 *
 * @param {FileRequest} request
 *
 * @returns {Promise<void>}
 */
export async function upload (request) {
  await axios.post('http://localhost:8080/upload', request.bytes.buffer, {
    headers: {
      'X-File-Id': request.id,
      'X-File-Name': request.name,
      'Content-Type': request.mimeType,
    },
    onUploadProgress: (progressEvent) => {
      if (progressEvent.total) {
        const percent = (progressEvent.loaded / progressEvent.total) * 100
        const elapsedTime = (performance.now() - startTime) / 1000 // Tiempo en segundos
        const speed = progressEvent.loaded / elapsedTime // Bytes por segundo

        console.log(`Progreso: ${percent.toFixed(2)}% - Velocidad: ${(speed / 1024).toFixed(2)} KB/s`)
      }
    },
  })
}
