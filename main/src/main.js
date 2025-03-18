import { app, BrowserWindow } from 'electron'
import fs from 'fs'
import { appDataDirectory } from './environment.js'
import { database } from './database.js'

function createWindow() {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    frame: false, // Oculta la barra de título
    titleBarStyle: 'hidden',
    titleBarOverlay: {
      color: 'rgba(0, 0, 0, 0)',
      symbolColor: '#ffffff',
      height: 32
    }
  })

  if (process.argv.indexOf('--dev-server') !== -1) {
    console.log('using vite dev server')
    win.webContents.openDevTools()
    win.loadURL('http://localhost:5173/')
  } else {
    win.loadFile('dist/index.html')
  }
}

function ensureAppDataDirectoryIsCreated() {
  const directory = appDataDirectory()

  if (!fs.existsSync(directory)) {
    fs.mkdirSync(directory, { recursive: true })
  }
}

app.whenReady().then(() => {
  ensureAppDataDirectoryIsCreated()
  createWindow()
})

app.on('quit', () => {
  database.close()
})