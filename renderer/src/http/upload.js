import { UploadRequest } from "../model/upload-request";
import { axiosInstance } from "./client";

/**
 * @typedef {(request: UploadRequest) => void} OnProgressCallback
 */

class UploadRequestState {
  #request;
  #notifyProgress;

  /**
   * Constructor
   * @param {UploadRequest} request
   * @param {OnProgressCallback} notifyProgress
   */
  constructor(request, notifyProgress) {
    this.#request = request;
    this.#notifyProgress = notifyProgress;
  }

  get request() {
    return this.#request;
  }

  /**
   * Update request state
   * @param {(state: UploadRequest) => UploadRequest} updater
   */
  update(updater) {
    this.#request = updater(this.#request);
    this.#notifyProgress(this.#request);
  }
}

/**
 * Upload file
 * @param {UploadRequest} request
 * @param {OnProgressCallback} onProgress
 */
export async function upload(request, onProgress = () => {}) {
  const state = new UploadRequestState(request, onProgress);
  if (!mimeTypeIsPresent(state)) {
    return;
  }

  try {
    const response = await axiosInstance.post(
      "http://localhost:8080/upload",
      state.request.file,
      {
        headers: {
          "X-File-Id": state.request.id,
          "X-File-Name": state.request.name,
          "Content-Type": state.request.mimeType,
        },
        onUploadProgress: (event) => {
          state.update((state) =>
            state.updateProgress({
              progress: event.progress,
              speed: event.rate,
            })
          );
        },
      }
    );

    if (response.status !== 201) {
      state.update((state) =>
        state.finishWithError(`Error uploading file: ${response.statusText}`)
      );
    }
  } catch (exception) {
    if (exception instanceof Error) {
      state.update((state) => state.finishWithError(exception.message));
    } else {
      state.update((state) =>
        state.finishWithError("An unknown error occurred during file upload")
      );
    }
  }

  state.update((state) => state.finishSuccess());
}

/**
 * Check if mime type is present in request
 * @param {UploadRequestState} state
 * @returns {boolean}
 */
function mimeTypeIsPresent(state) {
  if (state.request.mimeType == null) {
    state.update((state) => state.finishWithError("Unknown file type"));
    return false;
  }

  return true;
}
