import { BrowserWindow, nativeImage, Event } from 'electron'
import { iconPath, isDevMode, isPreviewMode, mainWindowHtmlPath, preloadPath } from './environment.ts'
import { appContext } from './app-context.ts'

export class MainWindow extends BrowserWindow {
	constructor() {
		super({
			width: 800,
			height: 600,
			frame: false, // Oculta la barra de título
			titleBarStyle: 'hidden',
			titleBarOverlay: {
				color: 'rgba(0, 0, 0, 0)',
				symbolColor: '#ffffff',
				height: 32
			},
			title: 'Miku Push!',
			icon: nativeImage.createFromPath(iconPath),
			webPreferences: {
				preload: preloadPath,
				devTools: isDevMode || isPreviewMode,
			}
		})
	}

	public initialize() {
		this.setupEventsListeners()
		this.loadHtml()
	}

	private setupEventsListeners() {
		this.on('close', (event) => this.onClose(event))
	}

	private onClose(event: Event) {
		if (!appContext.isQuitting) {
			event.preventDefault()
			this.hide()
		}
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
}
