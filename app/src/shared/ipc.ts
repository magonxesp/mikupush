import type { UploadModel } from '../main/database'
import type { NotificationOptions } from '../main/notification'

export interface UploadAPI {
	create(upload: UploadModel): void,
	delete(uploadId: string): void,
	findAll(): Promise<UploadModel[]>
}

export interface ClipBoardAPI {
	writeToClipboard(text: string): void
}

export interface NotificationAPI {
	showNotification(options: NotificationOptions): void
}
