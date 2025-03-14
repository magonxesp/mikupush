import { useState } from 'react'
import styles from './App.module.css'
import AppTabs from './components/AppTabs/AppTabs'
import AppTitle from './components/AppTitle/AppTitle'
import InputTab from './components/InputTab/InputTab'
import UploadsFinishedTab from './components/UploadsFinishedTab/UploadsFinishedTab'
import UploadsProgressTab from './components/UploadsProgressTab/UploadsProgressTab'

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

  const handleTabSelected = (index) => {
    setCurrentTab(index)
  }

  return (
    <div className={styles.app}>
      <AppTitle />
      <AppTabs tabs={tabs} onTabSelected={handleTabSelected} />
      <div className={styles.tabView}>
        {tabs[currentTab].component}
      </div>
    </div>
  )
} 

export default App
