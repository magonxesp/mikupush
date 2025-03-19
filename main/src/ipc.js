import { ipcMain } from 'electron'
import { deleteUpload, findAllUploads, insertUpload } from './database.js'

ipcMain.on('upload:create', (_, upload) => insertUpload(upload).catch(error => console.error(error)))
ipcMain.on('upload:delete', (_, uploadId) => deleteUpload(uploadId).catch(error => console.error(error)))
ipcMain.handle('upload:findAll', findAllUploads)
