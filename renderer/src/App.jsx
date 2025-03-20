import { useState } from "react";
import styles from "./App.module.css";
import AppTabs from "./components/AppTabs/AppTabs";
import AppTitle from "./components/AppTitle/AppTitle";
import InputTab from "./components/InputTab/InputTab";
import UploadsFinishedTab from "./components/UploadsFinishedTab/UploadsFinishedTab";
import UploadsProgressTab from "./components/UploadsProgressTab/UploadsProgressTab";
import { UploadsContext } from "./context/upload";
import { useUploadsContextState } from "./context/upload";

function App() {
  const tabs = {
    upload: <InputTab />,
    "uploads-in-progress": <UploadsProgressTab />,
    "finished-uploads": <UploadsFinishedTab />,
  };

  const [currentTab, setCurrentTab] = useState("upload");
  const uploadContext = useUploadsContextState(currentTab);

  const handleTabSelected = (index) => {
    setCurrentTab(index);
  };

  return (
    <UploadsContext.Provider value={uploadContext}>
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
