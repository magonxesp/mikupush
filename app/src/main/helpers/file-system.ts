import { FileDetails } from '../../shared/model/file-details.ts'
import fs from 'fs'
import path from 'path'
import { lookup } from 'mime-types'

export function appDataDirectory() {
	let appDataDir

	switch (process.platform) {
	case 'darwin':
		appDataDir = `${process.env.HOME}/Library/Application Support/io.mikupush.client`
		break
	case 'win32':
		appDataDir = `${process.env.APPDATA}\\Miku Push`
		break
	default:
		appDataDir = `${process.env.HOME}/.mikupush`
		break
	}

	console.log('application data directory', appDataDir)
	return appDataDir
}

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
