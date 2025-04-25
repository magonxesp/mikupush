import { Tray, Menu, nativeImage, BrowserWindow, app } from 'electron'
import path from 'path'

export const setupTray = (window: BrowserWindow) => {
	const tray = new Tray(nativeImage.createFromPath(path.join(__dirname, 'assets/icon.png')))

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
