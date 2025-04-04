import { ipcMain, clipboard } from 'electron'
import { deleteUpload, findAllUploads, insertUpload, type UploadModel } from './database'
import { notify, type NotificationOptions } from './notification'

ipcMain.on('upload:create', (_, upload: UploadModel) => insertUpload(upload).catch(error => console.error(error)))
ipcMain.on('upload:delete', (_, uploadId: string) => deleteUpload(uploadId).catch(error => console.error(error)))
ipcMain.handle('upload:findAll', findAllUploads)

ipcMain.on('clipboard:write', (_, text: string) => clipboard.writeText(text))
ipcMain.on('notification:show', (_, options: NotificationOptions) => notify(options))