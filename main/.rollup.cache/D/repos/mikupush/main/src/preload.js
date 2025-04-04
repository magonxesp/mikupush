import { contextBridge, ipcRenderer } from 'electron';
contextBridge.exposeInMainWorld('uploadAPI', {
    create: (upload) => ipcRenderer.send('upload:create', upload),
    delete: (uploadId) => ipcRenderer.send('upload:delete', uploadId),
    findAll: () => ipcRenderer.invoke('upload:findAll')
});
contextBridge.exposeInMainWorld('clipBoardAPI', {
    writeToClipboard: (text) => ipcRenderer.send('clipboard:write', text)
});
contextBridge.exposeInMainWorld('notificationAPI', {
    showNotification: (options) => ipcRenderer.send('notification:show', options)
});
//# sourceMappingURL=preload.js.map