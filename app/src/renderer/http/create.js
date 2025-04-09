import { UploadRequest } from '../model/upload-request'
import { axiosInstance, serverBaseUrl } from './client'

/**
 * Create file upload.
 * @param {UploadRequest} request 
 */
export async function create(request) {
  const data = {
    'uuid': request.id,
    'name': request.name,
    'mime_type': request.mimeType,
    'size': request.file.size
  }

  const response = await axiosInstance.post(`${serverBaseUrl}/api/file`, data, {
    headers: {
      "Accept": "application/json",
      "Content-Type": "application/json",
    },
  })

  if (response.status !== 200) {
    throw new Error(`file create request failed with status ${response.status}`)
  }
}