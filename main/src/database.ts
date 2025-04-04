import { DataTypes, Model, Sequelize, type InferAttributes, type InferCreationAttributes } from 'sequelize'
import path from 'path'
import { appDataDirectory } from './environment'

export const database = new Sequelize({
  dialect: 'sqlite',
  storage: path.join(appDataDirectory(), 'data.db')
});

export interface UploadModel extends Model<InferAttributes<UploadModel>, InferCreationAttributes<UploadModel>> {
  id: string;
  name: string;
  size: number;
  mimeType: string;
  uploadedAt: Date;
}

const Upload = database.define<UploadModel>('Upload', {
  id: {
    type: DataTypes.UUIDV4,
    primaryKey: true,
  },
  name: DataTypes.STRING,
  size: DataTypes.BIGINT,
  mimeType: DataTypes.STRING,
  uploadedAt: DataTypes.DATE,
})

export async function insertUpload(upload: UploadModel) {
  await Upload.create({
    id: upload.id,
    name: upload.name,
    size: upload.size,
    mimeType: upload.mimeType,
    uploadedAt: upload.uploadedAt,
  })
}

export async function findAllUploads() {
  const uploads = await Upload.findAll({
    order: [
      ['uploadedAt', 'DESC']
    ]
  })

  return uploads.map(upload => ({
    id: upload.id,
    name: upload.name,
    size: upload.size,
    mimeType: upload.mimeType,
    uploadedAt: upload.uploadedAt
  }))
}

export async function deleteUpload(uploadId: string) {
  await Upload.destroy({
    where: {
      id: uploadId
    }
  })
}