import { ClipBoardAPI } from '../../shared/ipc'

const clipboardApiDefaults: ClipBoardAPI = {
	writeToClipboard: () => {},
}

const clipboardApi = window.clipBoardAPI ?? clipboardApiDefaults

export const { writeToClipboard } = clipboardApi
