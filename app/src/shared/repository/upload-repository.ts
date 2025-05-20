import { Upload } from '../model/upload.ts'

export interface UploadRepository {
	findAll(): Promise<Upload[]>
	delete(uploadId: string): Promise<void>
	save(upload: Upload): Promise<void>
	findById(uploadId: string): Promise<Upload | null>
}
