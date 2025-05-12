import {
	SystemChannels, systemChannelsName,
	systemCopyToClipboardChannel,
	systemShowNotificationChannel
} from '../shared/ipc/system-channels.ts'
import { contextBridge, ipcRenderer, webUtils } from 'electron'

const systemChannels: SystemChannels = {
	copyToClipboard: (text) => ipcRenderer.send(systemCopyToClipboardChannel, text),
	showNotification: (options) => ipcRenderer.send(systemShowNotificationChannel, options),
	resolveWebFilePath: (file) => webUtils.getPathForFile(file),
}

contextBridge.exposeInMainWorld(systemChannelsName, systemChannels)
