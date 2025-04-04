import { Tray, Menu, nativeImage, BrowserWindow } from 'electron'
import path from 'path'

const contextMenu = Menu.buildFromTemplate([
    { label: 'Item1', type: 'radio' },
    { label: 'Item2', type: 'radio' },
    { label: 'Item3', type: 'radio', checked: true },
    { label: 'Item4', type: 'radio' }
])

export const appTray = (window: BrowserWindow) => {
    return new Tray(nativeImage.createFromPath(path.join(__dirname, '/assets/tray_icon.png')))
}
