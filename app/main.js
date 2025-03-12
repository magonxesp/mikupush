import { app, BrowserWindow, ipcMain, dialog } from 'electron'
import * as path from "node:path";

const createWindow = () => {
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

app.whenReady().then(() => {
  createWindow()
})
