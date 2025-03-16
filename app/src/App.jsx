import { useRef, useState } from 'react'
import styles from './App.module.css'
import AppTabs from './components/AppTabs/AppTabs'
import AppTitle from './components/AppTitle/AppTitle'
import InputTab from './components/InputTab/InputTab'
import UploadsFinishedTab from './components/UploadsFinishedTab/UploadsFinishedTab'
import UploadsProgressTab from './components/UploadsProgressTab/UploadsProgressTab'
import { UploadsContext } from './context'
import { addToUploadQueue } from './helpers/upload'

function App() {
  const tabs = {
    'upload': <InputTab />,
    'uploads-in-progress': <UploadsProgressTab />,
    'finished-uploads': <UploadsFinishedTab />
  }

  const [currentTab, setCurrentTab] = useState('upload')
  const [inProgressUploads, setInProgressUploads] = useState([])
  const [finishedUploads, setFinishedUploads] = useState([])
  const [inProgressUploadsCount, setInProgressUploadsCount] = useState(0)
  const [finishedUploadsCount, setFinishedUploadsCount] = useState(0)

  const handleTabSelected = (index) => {
    setCurrentTab(index)
  }

  const onUploadProgressUpdated = (upload) => {
    if (upload.finished && upload.error === '') {
      setInProgressUploads(previous => previous.filter(item => item.id !== upload.id))
      setFinishedUploads(previous => [...previous, upload])
      setInProgressUploadsCount(previous => (previous > 0) ? previous - 1 : previous)

      if (currentTab !== 'finished-uploads') {
        setFinishedUploadsCount(previous => previous + 1)
      }
    } else {
      setInProgressUploads(previous => previous.map(item => (item.id === upload.id) ? upload : item))
    }
  }

  const handleUploadRequest = async (file) => {
    try {
      const upload = await addToUploadQueue(file, onUploadProgressUpdated)
      setInProgressUploads(previous => [...previous, upload])

      if (currentTab !== 'uploads-in-progress') {
        setInProgressUploadsCount(previous => previous + 1)
      }

      console.log('added file to upload queue')
    } catch (exception) {
      console.error('unable to upload file', exception)
    }
  }

  const handleCancelUpload = (upload) => {
    console.log('cancel upload', upload)
    setInProgressUploads(inProgressUploads.filter(item => item.id !== upload.id))
  }

  const handleRetryUpload = (upload) => {
    console.log('on retry', upload)
  }

  return (
    <UploadsContext.Provider
      value={{
        inProgressUploads,
        finishedUploads,
        requestUpload: handleUploadRequest,
        cancelUpload: handleCancelUpload,
        retryUpload: handleRetryUpload,
        inProgressUploadsCount: inProgressUploadsCount,
        finishedUploadsCount: finishedUploadsCount,
        resetInProgressUploadsCount: () => setInProgressUploadsCount(0),
        resetFinishedUploadsCount: () => setFinishedUploadsCount(0)
      }}
    >
      <div className={styles.app}>
        <div>
          <div className={styles.dragArea} />
          <AppTitle />
          <AppTabs onTabSelected={handleTabSelected} />
        </div>
        <div className={styles.content}>
          {tabs[currentTab]}
        </div>
      </div>
    </UploadsContext.Provider>
  )
} 

export default App
