import { Notifier } from '../ports/notifier.ts'
import { UploadClient } from '../client/upload-client.ts'
import { Clipboard } from '../ports/clipboard.ts'
import { UploadRepository } from '../repository/upload-repository.ts'

export class UploadLinkGrabber {
	private readonly notifier: Notifier
	private readonly uploadRepository: UploadRepository
	private readonly uploadClient: UploadClient
	private readonly clipboard: Clipboard

	constructor(
		notifier: Notifier,
		uploadRepository: UploadRepository,
		uploadClient: UploadClient,
		clipboard: Clipboard
	) {
		this.notifier = notifier
		this.uploadRepository = uploadRepository
		this.uploadClient = uploadClient
		this.clipboard = clipboard
	}

	async grabLink(uploadId: string) {
		const upload = await this.uploadRepository.findById(uploadId)

		if (!upload) {
			console.error(`failed copy link for upload with id: ${uploadId}`)
			return
		}

		const link = this.uploadClient.link(uploadId)
		await this.clipboard.copyText(link)

		this.notifier.showNotification({
			title: '📎Link copied to clipboard!',
			body: `The link for ${upload.name} is in the clipboard`
		})
	}
}
