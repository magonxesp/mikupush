import { ipcMain } from 'electron'
import type { NotificationOptions } from '../../shared/model/notification.ts'
import { systemCopyToClipboardChannel, systemShowNotificationChannel } from '../../shared/ipc/system-channels.ts'
import { systemClipboard, systemNotifier } from '../service-container.ts'

export class SystemChannelsBinder {
	static bind() {
		ipcMain.on(systemCopyToClipboardChannel, (_, text: string) => {
			systemClipboard.copyText(text)
		})

		ipcMain.on(systemShowNotificationChannel, (_, options: NotificationOptions) => {
			systemNotifier.showNotification(options)
		})
	}
}
