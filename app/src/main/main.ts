import { app } from 'electron'
import squirrelEvent from 'electron-squirrel-startup'
import { isDevMode } from './environment.ts'
import { Application } from './app.ts'

if (squirrelEvent) {
	app.quit()
}

if (isDevMode) {
	console.info('dev mode enabled')
}

Application.run()
