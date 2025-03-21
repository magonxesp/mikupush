import { Notification } from "electron";

export function notify({ title, body }) {
  const notification = new Notification({
    title,
    body,
  });

  notification.show();
}
