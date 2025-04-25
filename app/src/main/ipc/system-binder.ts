import { clipboard, ipcMain } from 'electron'
import type { NotificationOptions } from '../../shared/model/notification.ts'
import { showNotify } from '../services/notification.ts'
import { systemCopyToClipboardChannel, systemShowNotificationChannel } from '../../shared/ipc/system-channels.ts'

export class SystemChannelsBinder {
	static bind() {
		ipcMain.on(systemCopyToClipboardChannel, (_, text: string) => {
			clipboard.writeText(text)
		})

		ipcMain.on(systemShowNotificationChannel, (_, options: NotificationOptions) => {
			showNotify(options)
		})
	}
}
