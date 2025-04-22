import { Upload } from '../../shared/model/upload.ts'

const uploadApiDefaults = {
	create: () => {},
	delete: () => {},
	findAll: () => {}
}

const uploadApi = window.uploadAPI ?? uploadApiDefaults

/**
 * Save upload to local database
 * @param {Upload} upload
 */
export const createUpload = (upload) => uploadApi.create(upload.toPlainObject())
export const deleteUpload = uploadApi.delete
/**
 * Find all uploads
 * @returns {Promise<Upload[]>}
 */
export const findAllUploads = async () => (await uploadApi.findAll())?.map(item => new Upload(item))
