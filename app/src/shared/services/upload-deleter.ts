import { UploadRepository } from '../repository/upload-repository.ts'
import { UploadClient } from '../client/upload-client.ts'

export class UploadDeleter {
	private readonly uploadRepository: UploadRepository
	private readonly uploadClient: UploadClient

	constructor(uploadRepository: UploadRepository, uploadClient: UploadClient) {
		this.uploadRepository = uploadRepository
		this.uploadClient = uploadClient
	}

	async delete(uploadId: string) {
		await this.uploadClient.delete(uploadId)
		await this.uploadRepository.delete(uploadId)
	}
}
