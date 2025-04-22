import { UploadRequest } from '../model/upload-request.ts'
import { axiosInstance, serverBaseUrl } from './client.ts'

export async function upload(
	request: UploadRequest,
	onProgress: (event: {progress: number, speed: number}) => void = () => {}
) {
	if (request.mimeType == null) {
		throw new Error('unknown file type')
	}

	const response = await axiosInstance.post(
		`${serverBaseUrl}/api/file/${request.id}/upload`,
		request.file,
		{
			headers: {
				'Content-Type': request.mimeType,
			},
			signal: request.controller.signal,
			onUploadProgress: (event) => {
				if (event.progress != null && event.rate != null) {
					onProgress({ progress: event.progress, speed: event.rate })
				}
			},
		}
	)

	if (response.status !== 200) {
		throw new Error(`error uploading file: ${response.statusText}`)
	}
}
