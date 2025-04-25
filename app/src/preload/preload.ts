import { contextBridge, ipcRenderer } from 'electron'
import type { NotificationOptions } from '../main/notification'
import { ClipBoardAPI, NotificationAPI, UploadAPI } from '../shared/ipc.ts'

const uploadAPI: UploadAPI = {
	create: (upload: UploadModel) => ipcRenderer.send('upload:create', upload),
	delete: (uploadId: string) => ipcRenderer.send('upload:delete', uploadId),
	findAll: (): Promise<UploadModel[]> => ipcRenderer.invoke('upload:findAll')
}

const clipBoardAPI: ClipBoardAPI = {
	writeToClipboard: (text: string) => ipcRenderer.send('clipboard:write', text)
}

const notificationAPI: NotificationAPI = {
	showNotification: (options: NotificationOptions) => ipcRenderer.send('notification:show', options)
}

contextBridge.exposeInMainWorld('uploadAPI', uploadAPI)
contextBridge.exposeInMainWorld('clipBoardAPI', clipBoardAPI)
contextBridge.exposeInMainWorld('notificationAPI', notificationAPI)
