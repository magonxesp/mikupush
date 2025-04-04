import { Notification } from "electron";
export function notify(options) {
    const notification = new Notification({
        title: options.title,
        body: options.body,
    });
    notification.show();
}
//# sourceMappingURL=notification.js.map