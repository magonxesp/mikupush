import { NotificationOptions } from '../model/notification.ts'

export interface Notifier {
	showNotification(options: NotificationOptions): void
}
