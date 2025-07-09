import { BrowserWindow, nativeImage, Event, nativeTheme } from 'electron'
import { iconPath, isDevMode, isPreviewMode, mainWindowHtmlPath, preloadPath } from './environment.ts'
import { appContext } from './app-context.ts'

export class MainWindow extends BrowserWindow {
	constructor() {
		super({
			width: 800,
			height: 600,
			frame: false, // Oculta la barra de título
			titleBarStyle: 'hidden',
			titleBarOverlay: (process.platform !== 'darwin') ? true : {},
			title: 'Miku Push!',
			icon: nativeImage.createFromPath(iconPath),
			webPreferences: {
				preload: preloadPath,
				devTools: isDevMode || isPreviewMode,
			}
		})
	}

	public initialize() {
		this.updateTitleBarOverlay()
		this.setupEventsListeners()
		this.loadHtml()
	}

	private setupEventsListeners() {
		this.on('close', (event) => this.onClose(event))
		nativeTheme.on('updated', () => this.onNativeThemeUpdated())
	}

	private onClose(event: Event) {
		if (!appContext.isQuitting) {
			event.preventDefault()
			this.hide()
		}
	}

	private onNativeThemeUpdated() {
		this.updateTitleBarOverlay()
	}

	private loadHtml() {
		if (process.env.VITE_DEV_SERVER_URL) {
			console.log('using vite dev server')
			this.webContents.openDevTools()
			this.loadURL(process.env.VITE_DEV_SERVER_URL)
		} else {
			this.loadFile(mainWindowHtmlPath)
		}
	}

	private updateTitleBarOverlay() {
		if (process.platform === 'darwin') {
			return
		}

		this.setTitleBarOverlay({
			color: nativeTheme.shouldUseDarkColors ? '#1C1B1F' : '#FFFFFF',
			symbolColor: nativeTheme.shouldUseDarkColors ? '#FFFFFF' : '#000000',
		})
	}
}
