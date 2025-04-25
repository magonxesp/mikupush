import { Sequelize } from 'sequelize'
import path from 'path'
import { appDataDirectory } from './file-system.ts'

export const database = new Sequelize({
	dialect: 'sqlite',
	storage: path.join(appDataDirectory(), 'data.db')
})
