import { Tray, Menu, nativeImage, BrowserWindow, app } from 'electron'
import { iconPath } from './environment.ts'

export const setupTray = (window: BrowserWindow) => {
	const tray = new Tray(nativeImage.createFromPath(iconPath))

	const contextMenu = Menu.buildFromTemplate([
		{ label: 'Open', click: () => window.show() },
		{ label: 'Exit', click: () => app.exit() },
	])

	tray.setToolTip('Miku Push!')
	tray.setContextMenu(contextMenu)
    
	tray.addListener('double-click', () => {
		window.show()
	})
}
