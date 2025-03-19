import { DataTypes, Sequelize } from 'sequelize'
import path from 'path'
import { appDataDirectory } from './environment.js'

export const database = new Sequelize({
  dialect: 'sqlite',
  storage: path.join(appDataDirectory(), 'data.db')
});

const Upload = database.define('Upload', {
  id: {
    type: DataTypes.UUIDV4,
    primaryKey: true,
  },
  name: DataTypes.STRING,
  size: DataTypes.BIGINT,
  mimeType: DataTypes.STRING,
  uploadedAt: DataTypes.DATE,
})

export async function insertUpload(upload) {
  console.log('upload main process', upload)
  await Upload.create({
    id: upload.id,
    name: upload.name,
    size: upload.size,
    mimeType: upload.mimeType,
    uploadedAt: upload.uploadedAt,
  })
}