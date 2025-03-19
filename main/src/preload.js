/**
 * Preload.js
 * 
 * Here is not allowed to import ES modules.
 * Only can be imported a few electron functions.
 */
const { contextBridge, ipcRenderer } = require('electron')

contextBridge.exposeInMainWorld('uploadAPI', {
  create: (upload) => ipcRenderer.send('upload:create', upload),
  delete: (uploadId) => ipcRenderer.send('upload:delete', uploadId),
  findAll: () => ipcRenderer.invoke('upload:findAll')
})