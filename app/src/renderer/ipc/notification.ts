import { NotificationAPI } from '../../shared/ipc.ts'

const notificationApiDefaults: NotificationAPI = {
	showNotification: () => {}
}

const notificationsApi = window.notificationAPI ?? notificationApiDefaults

export const { showNotification } = notificationsApi
