import { Upload } from '../../shared/model/upload'
import { UploadModel } from '../../main/database'

const uploadApi = window.uploadAPI

export const createUpload = (upload: Upload) => uploadApi.create(upload.toPlainObject() as UploadModel)
export const deleteUpload = uploadApi.delete
export const findAllUploads = async () => (await uploadApi.findAll()).map(item => new Upload(item))
