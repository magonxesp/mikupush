import {
	uploadAbortChannel,
	UploadChannels,
	uploadChannelsName, uploadCopyLinkChannel, uploadDeleteChannel,
	uploadEnqueueChannel,
	uploadFindAllChannel,
	uploadOnProgressChannel,
	uploadRetryChannel
} from '../shared/ipc/upload-channels.ts'
import { SerializableUploadRequest } from '../shared/model/upload-request.ts'
import { contextBridge, ipcRenderer } from 'electron'

const uploadChannel: UploadChannels = {
	enqueue: (filePaths) => {
		return ipcRenderer.invoke(uploadEnqueueChannel, filePaths)
	},
	retry: (request) => {
		ipcRenderer.send(uploadRetryChannel, request)
	},
	findAll: () => {
		return ipcRenderer.invoke(uploadFindAllChannel)
	},
	onUploadProgress: (callback: (request: SerializableUploadRequest) => void) => {
		ipcRenderer.on(uploadOnProgressChannel, (_, request) => {
			callback(request)
		})
	},
	abort: (uploadId) => {
		ipcRenderer.send(uploadAbortChannel, uploadId)
	},
	delete: (uploadId) => {
		return ipcRenderer.invoke(uploadDeleteChannel, uploadId)
	},
	copyLink: (uploadId) => {
		ipcRenderer.send(uploadCopyLinkChannel, uploadId)
	}
}

contextBridge.exposeInMainWorld(uploadChannelsName, uploadChannel)
