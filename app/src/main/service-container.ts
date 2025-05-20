import { Uploader } from '../shared/services/uploader.ts'
import { SequelizeUploadRepository } from './repository/sequelize-upload-repository.ts'
import { UploadRepository } from '../shared/repository/upload-repository.ts'
import { SystemNotifier } from './adapter/system-notifier.ts'
import { UploadDeleter } from '../shared/services/upload-deleter.ts'
import { UploadClient } from '../shared/client/upload-client.ts'
import { serverBaseUrl } from './configuration.ts'
import { SystemClipboard } from './adapter/system-clipboard.ts'
import { UploadLinkGrabber } from '../shared/services/upload-link-grabber.ts'

/**
 * Adapters
 */
export const systemClipboard = new SystemClipboard()
export const systemNotifier = new SystemNotifier()

/**
 * Clients
 */
export const uploadClient = new UploadClient(serverBaseUrl())

/**
 * Repositories
 */
export const uploadRepository: UploadRepository = new SequelizeUploadRepository()

/**
 * Services
 */
export const uploader = new Uploader(uploadRepository, systemNotifier, uploadClient)
export const uploadDeleter = new UploadDeleter(uploadRepository, uploadClient)
export const uploadLinkGrabber = new UploadLinkGrabber(systemNotifier, uploadRepository, uploadClient, systemClipboard)
