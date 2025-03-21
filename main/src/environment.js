import { nativeImage } from 'electron'
import { fileURLToPath } from 'url'
import path from 'path'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

export function appDataDirectory() {
  switch (process.platform) {
    case 'darwin':
      return `${process.env.HOME}/Library/Application Support/io.mikupush.app`
    case 'win32':
      return `${process.env.APPDATA}\\Miku Push`
    default:
      return `${process.env.HOME}/.mikupush`
  }
}

export function appIcon64() {
  if (process.platform === 'win32') {
      return nativeImage.createFromPath(path.join(__dirname, '/assets/app-icon-64.ico'))
  } else {
      return nativeImage.createFromPath(path.join(__dirname, '/assets/app-icon-64.png'))
  }
}