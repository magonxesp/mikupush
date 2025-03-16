import { createContext } from "react";

export const UploadsContext = createContext({
    inProgressUploads: [],
    inProgressUploadsCount: 0,
    finishedUploads: [],
    finishedUploadsCount: 0,
    requestUpload: () => {},
    cancelUpload: () => {},
    retryUpload: () => {},
    resetInProgressUploadsCount: () => {},
    resetFinishedUploadsCount: () => {},
})