import { deleteUpload as httpDeleteUpload } from '../http/delete'
import { UploadRepository } from '../repository/upload-repository.ts'
import { Upload } from '../model/upload.ts'

export class UploadDeleter {
	private readonly uploadRepository: UploadRepository

	constructor(uploadRepository: UploadRepository) {
		this.uploadRepository = uploadRepository
	}

	async delete(upload: Upload) {
		await httpDeleteUpload(upload.id)
		await this.uploadRepository.delete(upload)
	}
}
