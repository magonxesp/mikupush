import fs from 'fs'

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
