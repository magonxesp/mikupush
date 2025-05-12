import { UploadRequest } from '../model/upload-request'
import { axiosInstance, serverBaseUrl } from './client'
import fs from 'fs'

export async function upload(
	request: UploadRequest,
	file: File | Buffer | fs.ReadStream,
	onProgress: (event: {progress: number, speed: number}) => void = () => {}
) {
	if (request.mimeType == null) {
		throw new Error('unknown file type')
	}

	const response = await axiosInstance.post(
		`${serverBaseUrl()}/api/file/${request.id}/upload`,
		file,
		{
			headers: {
				'Content-Type': request.mimeType,
			},
			signal: request.controller.signal,
			onUploadProgress: (event) => {
				onProgress({
					progress: event.progress ?? event.loaded / request.file.size,
					speed: event.rate ?? 0
				})
			},
		}
	)

	if (response.status !== 200) {
		throw new Error(`error uploading file: ${response.statusText}`)
	}
}
