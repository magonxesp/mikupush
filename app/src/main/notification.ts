import { Notification } from 'electron'

export interface NotificationOptions {
  title: string;
  body: string;
}

export function notify(options: NotificationOptions) {
	if (!Notification.isSupported()) {
		return
	}

	const notification = new Notification({
		title: options.title,
		body: options.body,
	})

	notification.show()
}
