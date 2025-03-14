import { createContext } from "react";

export const UploadsContext = createContext({
    inProgressUploads: [],
    finishedUploads: []
})