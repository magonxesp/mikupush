import { Uploader } from '../shared/services/uploader.ts'
import { SequelizeUploadRepository } from './repository/sequelize-upload-repository.ts'
import { UploadRepository } from '../shared/repository/upload-repository.ts'
import { SystemNotifier } from './adapter/system-notifier.ts'

/**
 * Adapters
 */
const systemNotifier = new SystemNotifier()

/**
 * Repositories
 */
export const uploadRepository: UploadRepository = new SequelizeUploadRepository()

/**
 * Services
 */
export const uploader = new Uploader(uploadRepository, systemNotifier)
