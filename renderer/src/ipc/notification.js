const notificationApiDefaults = {
    showNotification: () => {}
}

const notificationsApi = window.notificationAPI ?? notificationApiDefaults

export const { showNotification } = notificationsApi
