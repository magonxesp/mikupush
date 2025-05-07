import {
	UploadChannels,
	uploadChannelsName, uploadEnqueueChannel,
	uploadFindAllChannel, uploadOnProgressChannel,
	uploadRetryChannel
} from '../shared/ipc/upload-channels.ts'
import { UploadRequest } from '../shared/model/upload-request.ts'
import { contextBridge, ipcRenderer } from 'electron'
import { Upload } from '../shared/model/upload.ts'
import { ClassProperties } from '../shared/model/properties.ts'

const uploadChannel: UploadChannels = {
	enqueue: (filePaths: string[]): Promise<UploadRequest[]> => {
		return ipcRenderer.invoke(uploadEnqueueChannel, filePaths)
	},
	retry: (request: UploadRequest) => {
		ipcRenderer.send(uploadRetryChannel, request)
	},
	findAll: (): Promise<Upload[]> => {
		return ipcRenderer.invoke(uploadFindAllChannel)
	},
	onUploadProgress: (callback: (request: ClassProperties<UploadRequest>) => void) => {
		ipcRenderer.on(uploadOnProgressChannel, (_, request: ClassProperties<UploadRequest>) => {
			callback(request)
		})
	},
}

contextBridge.exposeInMainWorld(uploadChannelsName, uploadChannel)
