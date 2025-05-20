import { clipboard } from 'electron'
import { Clipboard } from '../../shared/ports/clipboard.ts'

export class SystemClipboard implements Clipboard {
	async copyText(text: string) {
		clipboard.writeText(text)
	}
}
