import { SerializableUploadRequest } from '../model/upload-request.ts'
import { SerializableUpload } from '../model/upload.ts'

export interface UploadChannels {
	enqueue(filePaths: string[]): Promise<SerializableUploadRequest[]>
	retry(request: SerializableUploadRequest): void
	findAll(): Promise<SerializableUpload[]>
	onUploadProgress(callback: (request: SerializableUploadRequest) => void): void
	abort(uploadId: string): void
}

export const uploadChannelsName = 'uploadChannels'
export const uploadEnqueueChannel = 'upload-equeue'
export const uploadRetryChannel = 'upload-retry'
export const uploadAbortChannel = 'upload-abort'
export const uploadFindAllChannel = 'upload-find-all'
export const uploadOnProgressChannel = 'upload-on-progress'

declare global {
	interface Window {
		[uploadChannelsName]: UploadChannels
	}
}
