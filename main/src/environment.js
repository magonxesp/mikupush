import { nativeImage } from 'electron'
import { fileURLToPath } from 'url'
import path from 'path'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

export function appDataDirectory() {
  let appDataDir

  switch (process.platform) {
    case 'darwin':
      appDataDir = `${process.env.HOME}/Library/Application Support/io.mikupush.MikuPush`
      break;
    case 'win32':
      appDataDir = `${process.env.APPDATA}\\Miku Push`
      break;
    default:
      appDataDir = `${process.env.HOME}/.mikupush`
      break;
  }

  console.log('application data directory', appDataDir)
  return appDataDir
}

export function appIcon64() {
  if (process.platform === 'win32') {
      return nativeImage.createFromPath(path.join(__dirname, '/assets/app-icon-64.ico'))
  } else {
      return nativeImage.createFromPath(path.join(__dirname, '/assets/app-icon-64.png'))
  }
}