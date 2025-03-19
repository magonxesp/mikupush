import { ipcMain } from 'electron'
import { insertUpload } from './database.js'

export const uploadCreateChannel = 'upload:create'

ipcMain.on(uploadCreateChannel, (upload) => insertUpload(upload))
