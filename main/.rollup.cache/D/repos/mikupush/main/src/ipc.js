import { ipcMain, clipboard } from 'electron';
import { deleteUpload, findAllUploads, insertUpload } from './database';
import { notify } from './notification';
ipcMain.on('upload:create', (_, upload) => insertUpload(upload).catch(error => console.error(error)));
ipcMain.on('upload:delete', (_, uploadId) => deleteUpload(uploadId).catch(error => console.error(error)));
ipcMain.handle('upload:findAll', findAllUploads);
ipcMain.on('clipboard:write', (_, text) => clipboard.writeText(text));
ipcMain.on('notification:show', (_, options) => notify(options));
//# sourceMappingURL=ipc.js.map