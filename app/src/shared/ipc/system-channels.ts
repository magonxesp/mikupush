import { NotificationOptions } from '../model/notification.ts'

export interface SystemChannels {
	showNotification(options: NotificationOptions): void
	copyToClipboard(text: string): void
	resolveWebFilePath(file: File): string
}

export const systemChannelsName = 'systemChannels'
export const systemShowNotificationChannel = 'system-show-notification'
export const systemCopyToClipboardChannel = 'system-copy-to-clipboard'

declare global {
	interface Window {
		[systemChannelsName]: SystemChannels
	}
}
