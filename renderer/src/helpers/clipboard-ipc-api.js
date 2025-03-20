const clipboardApiDefaults = {
  writeToClipboard: () => {},
}

function clipboardApi() {
  if (Object.hasOwn(window, 'clipBoardAPI')) {
    return window['clipBoardAPI']
  }

  return clipboardApiDefaults
}

export function writeToClipboard(text) {
  clipboardApi().writeToClipboard(text)
}