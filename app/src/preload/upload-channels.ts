import {
	UploadChannels,
	uploadChannelsName, uploadEnqueueChannel,
	uploadFindAllChannel, uploadOnProgressChannel,
	uploadRetryChannel
} from '../shared/ipc/upload-channels.ts'
import { UploadRequest } from '../shared/model/upload-request.ts'
import { contextBridge, ipcRenderer } from 'electron'
import { Upload } from '../shared/model/upload.ts'

const uploadChannel: UploadChannels = {
	enqueue: (filePath: string): Promise<UploadRequest> => {
		return ipcRenderer.invoke(uploadEnqueueChannel, filePath)
	},
	retry: (request: UploadRequest) => {
		ipcRenderer.send(uploadRetryChannel, request)
	},
	findAll: (): Promise<Upload[]> => {
		return ipcRenderer.invoke(uploadFindAllChannel)
	},
	onUploadProgress: (callback: (request: UploadRequest) => void) => {
		ipcRenderer.on(uploadOnProgressChannel, (_, request: UploadRequest) => {
			callback(request)
		})
	},
}

contextBridge.exposeInMainWorld(uploadChannelsName, uploadChannel)
