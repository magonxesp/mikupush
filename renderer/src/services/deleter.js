import { deleteUpload as ipcDeleteUpload } from '../ipc/upload'
import { deleteUpload as httpDeleteUpload } from '../http/delete.js'

export class Deleter {
  async delete(id) {
    await httpDeleteUpload(id);
    await ipcDeleteUpload(id);
  }
}