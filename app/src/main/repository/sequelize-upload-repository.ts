import { UploadRepository } from '../../shared/repository/upload-repository.ts'
import { Upload } from '../../shared/model/upload.ts'
import { DataTypes, type InferAttributes, type InferCreationAttributes, Model } from 'sequelize'
import { database } from '../helpers/database.ts'

export class SequelizeUploadRepository implements UploadRepository {
	async findById(id: string): Promise<Upload | null> {
		const uploadModel = await UploadDao.findOne({
			where: { id }
		})

		if (!uploadModel) {
			return null
		}

		return this.mapUploadModelToUpload(uploadModel)
	}

	async findAll(): Promise<Upload[]> {
		const uploads = await UploadDao.findAll({
			order: [
				['uploadedAt', 'DESC']
			]
		})

		return uploads.map(this.mapUploadModelToUpload)
	}

	async delete(uploadId: string): Promise<void> {
		await UploadDao.destroy({
			where: {
				id: uploadId
			}
		})
	}

	async save(upload: Upload): Promise<void> {
		const existing = await this.findById(upload.id)

		if (existing != null) {
			await UploadDao.update(upload, {
				where: { id: upload.id }
			})
		} else {
			await UploadDao.create(upload)
		}
	}

	private mapUploadModelToUpload(uploadModel: UploadModel): Upload {
		return new Upload({
			id: uploadModel.id,
			name: uploadModel.name,
			size: uploadModel.size,
			mimeType: uploadModel.mimeType,
			uploadedAt: uploadModel.uploadedAt,
		})
	}
}

interface UploadModel extends Model<InferAttributes<UploadModel>, InferCreationAttributes<UploadModel>> {
	id: string
	name: string
	size: number
	mimeType: string
	uploadedAt: Date
}

const UploadDao = database.define<UploadModel>('Upload', {
	id: {
		type: DataTypes.UUIDV4,
		primaryKey: true,
	},
	name: DataTypes.STRING,
	size: DataTypes.BIGINT,
	mimeType: DataTypes.STRING,
	uploadedAt: DataTypes.DATE,
})
