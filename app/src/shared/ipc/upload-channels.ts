import { UploadRequest } from '../model/upload-request.ts'
import { Upload } from '../model/upload.ts'

export interface UploadChannels {
	enqueue(filePath: string): Promise<UploadRequest>
	retry(request: UploadRequest): void
	findAll(): Promise<Upload[]>
	onUploadProgress(callback: (request: UploadRequest) => void): void
}

export const uploadChannelsName = 'uploadChannels'
export const uploadEnqueueChannel = 'upload-equeue'
export const uploadRetryChannel = 'upload-retry'
export const uploadFindAllChannel = 'upload-find-all'
export const uploadOnProgressChannel = 'upload-on-progress'

declare global {
	interface Window {
		[uploadChannelsName]: UploadChannels
	}
}
