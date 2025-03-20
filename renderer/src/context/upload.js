import { createContext } from "react";

export const UploadsContext = createContext({
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
});
