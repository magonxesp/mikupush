import { app, BrowserWindow } from 'electron'
import fs from 'fs'
import { appDataDirectory } from './environment.js'
import { database } from './database.js'
import path from 'path'
import './ipc.js'
import { fileURLToPath } from 'url'
import { appIcon64 } from './environment.js'
import { appTray } from './tray.js'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

const isDevMode = process.argv.indexOf('--dev-server') !== -1

if (isDevMode) {
  app.commandLine.appendSwitch("ignore-certificate-errors", "true");
}

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
    },
    icon: appIcon64(),
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  })

  if (isDevMode) {
    console.log('using vite dev server')
    win.webContents.openDevTools()
    win.loadURL('http://localhost:5173/')
  } else {
    win.loadFile('dist/index.html')
  }

  database.sync()
}

function ensureAppDataDirectoryIsCreated() {
  const directory = appDataDirectory()

  if (!fs.existsSync(directory)) {
    fs.mkdirSync(directory, { recursive: true })
  }
}

app.whenReady().then(() => {
  appTray()
  ensureAppDataDirectoryIsCreated()
  createWindow()
})

app.on('quit', () => {
  database.close()
})