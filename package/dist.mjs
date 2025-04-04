import fs from 'fs';

const rendererDist = '../renderer/dist'
const mainDist = '../main/dist'
const packageJson = '../main/package.json'
const distDir = 'dist'

if (fs.existsSync(distDir)) {
  fs.rmdirSync(distDir, { recursive: true })
}

fs.mkdirSync(distDir, {recursive: true})
fs.cpSync(mainDist, `${distDir}/dist`, {recursive: true})
fs.cpSync(rendererDist, `${distDir}/renderer`, { recursive: true })
fs.cpSync(packageJson, `${distDir}/package.json`, { recursive: true })
