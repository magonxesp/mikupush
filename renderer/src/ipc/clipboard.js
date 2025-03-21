const clipboardApiDefaults = {
  writeToClipboard: () => {},
}

const clipboardApi = window.clipBoardAPI ?? clipboardApiDefaults

export const { writeToClipboard } = clipboardApi
