import { Notifier } from '../../shared/ports/notifier.ts'
import { NotificationOptions } from '../../shared/model/notification.ts'
import { Notification } from 'electron'

export class SystemNotifier implements Notifier {
	showNotification(options: NotificationOptions): void {
		if (!Notification.isSupported()) {
			return
		}

		const notification = new Notification({
			title: options.title,
			body: options.body,
		})

		notification.show()
	}
}
