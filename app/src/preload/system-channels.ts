import {
	SystemChannels, systemChannelsName,
	systemCopyToClipboardChannel,
	systemShowNotificationChannel
} from '../shared/ipc/system-channels.ts'
import { NotificationOptions } from '../shared/model/notification.ts'
import { contextBridge, ipcRenderer } from 'electron'

const systemChannels: SystemChannels = {
	copyToClipboard: (text: string) => ipcRenderer.send(systemCopyToClipboardChannel, text),
	showNotification:(options: NotificationOptions) => ipcRenderer.send(systemShowNotificationChannel, options)
}

contextBridge.exposeInMainWorld(systemChannelsName, systemChannels)
