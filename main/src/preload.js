import { contextBridge, ipcRenderer } from 'electron'
import { uploadCreateChannel } from './ipc.js'

contextBridge.exposeInMainWorld('uploadAPI', {
  create: (upload) => ipcRenderer.send(uploadCreateChannel, upload)
})