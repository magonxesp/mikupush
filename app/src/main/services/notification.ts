import { Notification } from 'electron'
import { NotificationOptions } from '../../shared/model/notification.ts'

export function showNotify(options: NotificationOptions) {
	if (!Notification.isSupported()) {
		return
	}

	const notification = new Notification({
		title: options.title,
		body: options.body,
	})

	notification.show()
}
