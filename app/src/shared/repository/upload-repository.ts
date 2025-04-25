import { Upload } from '../model/upload.ts'

export interface UploadRepository {
	findAll(): Promise<Upload[]>
	delete(upload: Upload): Promise<void>
	save(upload: Upload): Promise<void>
}
