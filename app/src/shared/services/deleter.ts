import { deleteUpload as ipcDeleteUpload } from '../../renderer/ipc/upload'
import { deleteUpload as httpDeleteUpload } from '../http/delete'

export class Deleter {
	async delete(id: string) {
		await httpDeleteUpload(id)
		await ipcDeleteUpload(id)
	}
}
