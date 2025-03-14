import { useState } from 'react'
import styles from './App.module.css'
import AppTabs from './components/AppTabs/AppTabs'
import AppTitle from './components/AppTitle/AppTitle'
import InputTab from './components/InputTab/InputTab'
import UploadsFinishedTab from './components/UploadsFinishedTab/UploadsFinishedTab'
import UploadsProgressTab from './components/UploadsProgressTab/UploadsProgressTab'
import { UploadsContext } from './context'
import { addToUploadQueue } from './upload'

function App() {
  const tabs = [
    {
      title: 'Upload files',
      icon: 'upload',
      component: <InputTab />
    },
    {
      title: 'Uploads in progresss',
      icon: 'schedule',
      component: <UploadsProgressTab />
    },
    {
      title: 'Finished uploads',
      icon: 'check_circle',
      component: <UploadsFinishedTab />
    }
  ]

  const [currentTab, setCurrentTab] = useState(0)
  const [inProgressUploads, setInProgressUploads] = useState([])
  const [finishedUploads, setFinishedUploads] = useState([])

  const handleTabSelected = (index) => {
    setCurrentTab(index)
  }

  const onUploadProgressUpdated = (upload) => {
    const updateUploadItem = (previous) => {
      return previous.map(item => {
        if (item.id === upload.id) {
          return upload
        } else {
          return item
        }
      })
    }

    setInProgressUploads(updateUploadItem)
  }

  const handleUploadRequest = (file) => {
    addToUploadQueue(file, onUploadProgressUpdated).then((upload) => {
      setInProgressUploads([...inProgressUploads, upload])
    })
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
        retryUpload: handleRetryUpload
      }}
    >
      <div className={styles.app}>
        <AppTitle />
        <AppTabs tabs={tabs} onTabSelected={handleTabSelected} />
        <div className={styles.tabView}>
          {tabs[currentTab].component}
        </div>
      </div>
    </UploadsContext.Provider>
  )
} 

export default App
