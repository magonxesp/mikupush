import { deleteUpload as ipcDeleteUpload } from '../ipc/upload'
import { deleteUpload as httpDeleteUpload } from '../http/delete.js'

export class Deleter {
  async delete(id: string) {
    await httpDeleteUpload(id);
    await ipcDeleteUpload(id);
  }
}