import { Notification } from 'electron'

export interface NotificationOptions {
  title: string;
  body: string;
}

export function notify(options: NotificationOptions) {
	const notification = new Notification({
		title: options.title,
		body: options.body,
	})

	notification.show()
}
