import { useEffect, useState } from 'react'
import styles from "./App.module.css";
import AppTabs from "./components/AppTabs/AppTabs";
import AppTitle from "./components/AppTitle/AppTitle";
import InputTab from "./components/InputTab/InputTab";
import UploadsFinishedTab from "./components/UploadsFinishedTab/UploadsFinishedTab";
import UploadsProgressTab from "./components/UploadsProgressTab/UploadsProgressTab";
import { UploadsContext } from "./context/upload";
import { Uploader } from './services/uploader'
import { UploadRequest } from './model/upload-request'
import { showNotification } from "./ipc/notification";
import { Deleter } from './services/deleter.js'
import { findAllUploads } from './ipc/upload.js'

const uploader = new Uploader()
const deleter = new Deleter()

function App() {
  const tabs = {
    upload: <InputTab />,
    "uploads-in-progress": <UploadsProgressTab />,
    "finished-uploads": <UploadsFinishedTab />,
  };

  const [currentTab, setCurrentTab] = useState("upload");
  const [inProgressUploads, setInProgressUploads] = useState([]);
  const [finishedUploads, setFinishedUploads] = useState([]);
  const [inProgressUploadsCount, setInProgressUploadsCount] = useState(0);
  const [finishedUploadsCount, setFinishedUploadsCount] = useState(0);

  useEffect(() => {
    findAllUploads().then(uploads => setFinishedUploads(uploads))
  }, [])

  /**
   * Remove from in progress uploads and decrement in progress items count
   * @param {UploadRequest} request 
   */
  const moveRequestAsFinished = (request) => {
    setInProgressUploads((previous) =>
      previous.filter((item) => item.id !== request.id)
    );

    setInProgressUploadsCount((count) => count > 0 ? count - 1 : count);
    setFinishedUploads((previous) => [request.upload, ...previous]);

    if (currentTab !== "finished-uploads") {
      setFinishedUploadsCount((count) => count + 1);
    }

    showNotification({
      title: `The file ${request.name} has been uploaded!`,
      body: `Now, you can grab the link for share it!`
    })
  }

  /**
   * Handle upload request progress update
   * @param {UploadRequest} request 
   */
  const handleProgressUpdate = (request) => {
    if (request.finishedSuccess) {
      moveRequestAsFinished(request)
    } else {
      setInProgressUploads((previous) =>
        previous.map((item) => item.id === request.id ? request : item)
      );
    }
  }

  /**
   * Request file upload
   * @param {File[]} files 
   */
  const requestUploads = async (files) => {
    const newUploads = [];

    for (let file of files) {
      try {
        newUploads.push(await uploader.enqueue(file, handleProgressUpdate));
      } catch (exception) {
        console.error("unable to upload file", exception);
      }
    }

    setInProgressUploads((previous) => [...previous, ...newUploads]);

    if (currentTab !== "uploads-in-progress") {
      setInProgressUploadsCount((previous) => previous + newUploads.length);
    }

    if (newUploads.length === 1) {
      const request = newUploads[0]
      showNotification({ 
        title: `Uploading file ${request.name} 🚀`, 
        body: `The file ${request.name} is added to the upload queue`
      })
    }

    if (newUploads.length > 1) {
      showNotification({ 
        title: `Uploading ${newUploads.length} files 🚀`, 
        body: 'The files are added to the upload queue'
      })
    }
  }

  /**
   * Cancel upload request
   * @param {UploadRequest} request 
   */
  const cancelUpload = (request) => {
    console.log("cancel upload", request);
    setInProgressUploads(
      inProgressUploads.filter((item) => item.id !== request.id)
    );
  }

  /**
   * Retry upload request
   * @param {UploadRequest} request 
   */
  const retryUpload = (request) => {
    uploader.retry(request, handleProgressUpdate);
  }

  const resetInProgressUploadsCount = () => setInProgressUploadsCount(0)
  const resetFinishedUploadsCount = () => setFinishedUploadsCount(0)

  const handleTabSelected = (index) => {
    setCurrentTab(index);
  };

  const deleteUpload = async (id) => {
    await deleter.delete(id);

    setFinishedUploads((previous) =>
      previous.filter((item) => item.id !== id)
    );
  };

  return (
    <UploadsContext.Provider value={{
      inProgressUploads,
      finishedUploads,
      inProgressUploadsCount,
      finishedUploadsCount,
      requestUploads,
      cancelUpload,
      retryUpload,
      resetInProgressUploadsCount,
      resetFinishedUploadsCount,
      deleteUpload
    }}>
      <div className={styles.app}>
        <div>
          <div className={styles.dragArea} />
          <AppTitle />
          <AppTabs onTabSelected={handleTabSelected} />
        </div>
        <div className={styles.content}>{tabs[currentTab]}</div>
      </div>
    </UploadsContext.Provider>
  );
}

export default App;
