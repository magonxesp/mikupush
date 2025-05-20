import axios, { AxiosInstance } from 'axios'
import { UploadRequest } from '../model/upload-request.ts'
import fs from 'fs'

export interface UploadProgressEvent {
	progress: number
	speed: number
}

export type UploadProgressCallback = (event: UploadProgressEvent) => void

export class UploadClient {
	private http: AxiosInstance
	private baseUrl: string

	constructor(baseUrl: string) {
		this.http = this.createHttpClient(baseUrl)
		this.baseUrl = baseUrl
	}

	private createHttpClient(baseUrl: string): AxiosInstance {
		return axios.create({
			baseURL: baseUrl,
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json',
			},
		})
	}

	async create(request: UploadRequest) {
		const data = {
			'uuid': request.id,
			'name': request.name,
			'mime_type': request.mimeType,
			'size': request.file.size
		}

		const response = await this.http.post('/api/file', data)

		if (response.status !== 200) {
			throw new Error(`upload create request failed with status ${response.status}`)
		}
	}

	async delete(id: string) {
		const response = await this.http.delete(`/api/file/${id}`)

		if (response.status !== 200) {
			throw new Error(`upload delete request failed with status ${response.status}`)
		}
	}

	async upload(
		request: UploadRequest,
		file: File | Buffer | fs.ReadStream,
		onProgress: UploadProgressCallback = () => {}
	) {
		if (request.mimeType == null) {
			throw new Error('unknown file type')
		}

		const response = await this.http.post(`/api/file/${request.id}/upload`, file, {
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
		})

		if (response.status !== 200) {
			throw new Error(`error uploading file: ${response.statusText}`)
		}
	}

	link(uploadId: string) {
		return `${this.baseUrl}/u/${uploadId}`
	}
}
