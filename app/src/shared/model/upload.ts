import { v4 as uuidv4 } from 'uuid'
import { FileDetails } from './file-details.ts'

export interface UploadObject {
    id: string
    name: string
    size: number
    mimeType: string
    uploadedAt: Date
}

export class Upload {
	public readonly id: string
	public readonly name: string
	public readonly size: number
	public readonly mimeType: string
	public readonly uploadedAt: Date

	constructor({ id, name, size, mimeType, uploadedAt }: UploadObject) {
		this.id = id
		this.name = name
		this.size = size
		this.mimeType = mimeType
		this.uploadedAt = uploadedAt
	}

	toPlainObject(): UploadObject {
		return {
			id: this.id,
			name: this.name,
			size: this.size,
			mimeType: this.mimeType,
			uploadedAt: this.uploadedAt
		}
	}

	static async fromFileDetails(file: FileDetails) {
		return new Upload({
			id: uuidv4(),
			name: file.name,
			size: file.size,
			mimeType: file.mimeType,
			uploadedAt: new Date()
		})
	}
}
