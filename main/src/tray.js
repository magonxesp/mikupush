import { appIcon64 } from './environment.js'
import { Tray } from 'electron'

export const appTray = () => {
    return new Tray(appIcon64())
}