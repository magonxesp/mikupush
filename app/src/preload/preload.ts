import { contextBridge, ipcRenderer } from 'electron'
import type { UploadModel } from '../main/database'
import type { NotificationOptions } from '../main/notification'

contextBridge.exposeInMainWorld('uploadAPI', {
  create: (upload: UploadModel) => ipcRenderer.send('upload:create', upload),
  delete: (uploadId: string) => ipcRenderer.send('upload:delete', uploadId),
  findAll: () => ipcRenderer.invoke('upload:findAll')
})

contextBridge.exposeInMainWorld('clipBoardAPI', {
  writeToClipboard: (text: string) => ipcRenderer.send('clipboard:write', text)
})

contextBridge.exposeInMainWorld('notificationAPI', {
  showNotification: (options: NotificationOptions) => ipcRenderer.send('notification:show', options)
})
