import { app, BrowserWindow } from 'electron'
import fs from 'fs'
import { appDataDirectory } from './environment'
import { database } from './database'
import path from 'path'
import './ipc'
import { setupTray } from './tray'

const isDevMode = (process.env.ELECTRON_ENV ?? 'prod') === 'dev'

if (isDevMode) {
  app.commandLine.appendSwitch("ignore-certificate-errors", "true");
}

let isAppQuitting = false

function createWindow() {
  const window = new BrowserWindow({
    width: 800,
    height: 600,
    frame: false, // Oculta la barra de título
    titleBarStyle: 'hidden',
    titleBarOverlay: {
      color: 'rgba(0, 0, 0, 0)',
      symbolColor: '#ffffff',
      height: 32
    },
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  })

  if (isDevMode) {
    console.log('using vite dev server')
    window.webContents.openDevTools()
    window.loadURL('http://localhost:5173/')
  } else {
    window.loadFile('../renderer/index.html')
  }

  window.on('close', function (evt) {
    if (!isAppQuitting) {
        evt.preventDefault();
        window.hide()
    }
  });

  database.sync()
  return window
}

function ensureAppDataDirectoryIsCreated() {
  const directory = appDataDirectory()

  if (!fs.existsSync(directory)) {
    fs.mkdirSync(directory, { recursive: true })
  }
}

app.whenReady().then(() => {
  ensureAppDataDirectoryIsCreated()
  const window = createWindow()
  setupTray(window)  
})

app.on('quit', () => {
  database.close()
})

app.on('before-quit', function (evt) {
    isAppQuitting = true
});
