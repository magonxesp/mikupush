import { DataTypes, Model, Sequelize, type InferAttributes, type InferCreationAttributes } from 'sequelize'
import path from 'path'
import { appDataDirectory } from './environment'

export const database = new Sequelize({
	dialect: 'sqlite',
	storage: path.join(appDataDirectory(), 'data.db')
})


export async function insertUpload(upload: UploadModel) {
	await Upload.create(upload)
}

export async function findAllUploads() {

}

export async function deleteUpload(uploadId: string) {

}
