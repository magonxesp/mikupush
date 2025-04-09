export function appDataDirectory() {
  let appDataDir

  switch (process.platform) {
    case 'darwin':
      appDataDir = `${process.env.HOME}/Library/Application Support/io.mikupush.MikuPush`
      break;
    case 'win32':
      appDataDir = `${process.env.APPDATA}\\Miku Push`
      break;
    default:
      appDataDir = `${process.env.HOME}/.mikupush`
      break;
  }

  console.log('application data directory', appDataDir)
  return appDataDir
}
