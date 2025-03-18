import { DataTypes, Sequelize } from 'sequelize'
import path from 'path'
import { appDataDirectory } from './environment.js'

export const database = new Sequelize({
  dialect: 'sqlite',
  storage: path.join(appDataDirectory(), 'data.db')
});

const Upload = database.define('Upload', {
  id: DataTypes.UUIDV4,
  name: DataTypes.STRING,
  size: DataTypes.BIGINT,
  mimeType: DataTypes.STRING,
  uploadedAt: DataTypes.DATE,
})