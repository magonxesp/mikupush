import { UploadRequest } from "../model/upload-request";
import { axiosInstance } from "./client";

/**
 * Upload file
 * @param {UploadRequest} request
 * @param {({progress: number, speed: number}) => void} onProgress
 */
export async function upload(request, onProgress = () => {}) {
  if (request.mimeType == null) {
    throw new Error('unknown file type');
  }

  const response = await axiosInstance.post(
    `http://localhost:8080/api/file/${request.id}/upload`,
    request.file,
    {
      headers: {
        "X-File-Id": request.id,
        "X-File-Name": request.name,
        "Content-Type": request.mimeType,
      },
      onUploadProgress: (event) => {
        onProgress({ progress: event.progress, speed: event.rate })
      },
    }
  );

  if (response.status !== 200) {
    throw new Error(`error uploading file: ${response.statusText}`)
  }
}
