import { createContext, useState, useEffect } from "react";
import { Uploader } from "../services/uploader";
import { UploadRequest } from "../model/upload-request";

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

export function useUploadsContextState(currentTab) {
  const uploader = new Uploader()

  const [inProgressUploads, setInProgressUploads] = useState([]);
  const [finishedUploads, setFinishedUploads] = useState([]);
  const [inProgressUploadsCount, setInProgressUploadsCount] = useState(0);
  const [finishedUploadsCount, setFinishedUploadsCount] = useState(0);

  useEffect(() => {
    //findAllUploads().then((uploads) => setFinishedUploads(uploads));
  }, []);

  /**
   * Remove from in progress uploads and decrement in progress items count
   * @param {UploadRequest} request 
   */
  const moveRequestAsFinished = (request) => {
    setInProgressUploads((previous) =>
      previous.filter((item) => item.id !== request.id)
    );
    setFinishedUploads((previous) => [request.upload, ...previous]);
    setInProgressUploadsCount((previous) =>
      previous > 0 ? previous - 1 : previous
    );
  }

  uploader.addProgressListener((request) => {
    if (request.finishSuccess) {
      moveRequestAsFinished(request)

      if (currentTab !== "finished-uploads") {
        setFinishedUploadsCount((previous) => previous + 1);
      }
    } else {
      setInProgressUploads((previous) =>
        previous.map((item) => item.id === request.id ? request : item)
      );
    }
  })

  const handlers = {
    requestUploads: async (files) => {
      const newUploads = [];

      for (let file of files) {
        try {
          newUploads.push(await uploader.enqueue(file));
        } catch (exception) {
          console.error("unable to upload file", exception);
        }
      }

      setInProgressUploads((previous) => [...previous, ...newUploads]);

      if (currentTab !== "uploads-in-progress") {
        setInProgressUploadsCount((previous) => previous + newUploads.length);
      }
    },
    cancelUpload: (upload) => {
      console.log("cancel upload", upload);
      setInProgressUploads(
        inProgressUploads.filter((item) => item.id !== upload.id)
      );
    },
    retryUpload: (upload) => {
      console.log("on retry", upload);
    },
    resetInProgressUploadsCount: () => setInProgressUploadsCount(0),
    resetFinishedUploadsCount: () => setFinishedUploadsCount(0),
  };

  return {
    inProgressUploads,
    finishedUploads,
    inProgressUploadsCount,
    finishedUploadsCount,
    ...handlers,
  };
}
