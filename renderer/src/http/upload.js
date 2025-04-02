import { UploadRequest } from "../model/upload-request";
import { axiosInstance, serverBaseUrl } from './client'

/**
 * Upload file
 * @param {UploadRequest} request
 * @param {({progress: number, speed: number}) => void} onProgress
 * @returns {Promise<void>}
 */
export async function upload(request, onProgress = () => {}) {
  if (request.mimeType == null) {
    throw new Error('unknown file type');
  }

  const response = await axiosInstance.post(
    `${serverBaseUrl}/api/file/${request.id}/upload`,
    request.file,
    {
      headers: {
        "Content-Type": request.mimeType,
      },
      signal: request.controller.signal,
      onUploadProgress: (event) => {
        onProgress({ progress: event.progress, speed: event.rate })
      },
    }
  );

  if (response.status !== 200) {
    throw new Error(`error uploading file: ${response.statusText}`)
  }
}
