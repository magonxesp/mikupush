import path from 'path'
import { appEnv } from '../shared/helpers/environment.ts'

export const isDevMode = appEnv() === 'dev'
export const isPreviewMode = appEnv() === 'preview'
export const preloadPath = path.join(__dirname, 'preload.js')
export const iconPath = path.join(__dirname, 'assets/icon.png')
export const mainWindowHtmlPath = path.join(__dirname, '../dist/index.html')
