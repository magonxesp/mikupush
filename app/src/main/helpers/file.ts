import path from 'path'
import fs from 'fs'
import { FileDetails } from '../../shared/model/file-details.ts'
import { lookup } from 'mime-types'

export function fileDetails(filePath: string): FileDetails {
	const stats = fs.statSync(filePath)

	return {
		name: path.basename(filePath),
		path: filePath,
		size: stats.size,
		mimeType: lookup(filePath) || 'application/octet-stream',
		isDirectory: stats.isDirectory(),
	}
}
