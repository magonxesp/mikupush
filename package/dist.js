import fs from 'fs';

const rendererDist = '../renderer/dist'
const mainDist = '../main'
const distDir = 'dist'

if (fs.existsSync(distDir)) {
  fs.rmdirSync(distDir, { recursive: true })
}

fs.mkdirSync(distDir, {recursive: true})
fs.cpSync(mainDist, distDir, {recursive: true})
fs.cpSync(rendererDist, `${distDir}/dist`, { recursive: true })
