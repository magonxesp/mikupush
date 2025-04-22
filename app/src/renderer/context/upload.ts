import { createContext } from 'react'
import { Upload } from '../../shared/model/upload.ts'
import { UploadRequest } from '../../shared/model/upload-request.ts'

interface UploadsContextProps {
  inProgressUploads: UploadRequest[]
  inProgressUploadsCount: number
  finishedUploads: Upload[],
  finishedUploadsCount: number
  requestUploads: (files: File[]) => void
  cancelUpload: (request: UploadRequest) => void
  retryUpload: (request: UploadRequest) => void
  resetInProgressUploadsCount: () => void
  resetFinishedUploadsCount: () => void
  deleteUpload: (id: string) => void
}

export const UploadsContext = createContext<UploadsContextProps>({
	inProgressUploads: [],
	inProgressUploadsCount: 0,
	finishedUploads: [],
	finishedUploadsCount: 0,
	requestUploads: () => {},
	cancelUpload: () => {},
	retryUpload: () => {},
	resetInProgressUploadsCount: () => {},
	resetFinishedUploadsCount: () => {},
	deleteUpload: () => {},
})
