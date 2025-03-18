import { useContext } from "react";
import { UploadsContext } from "../../context";
import styles from "./AppTabs.module.css";

export default function AppTabs({ onTabSelected }) {
  const {
    inProgressUploadsCount,
    finishedUploadsCount,
    resetInProgressUploadsCount,
    resetFinishedUploadsCount,
  } = useContext(UploadsContext);

  const handleTabSelected = (index) => {
    if (typeof onTabSelected !== "undefined") {
      onTabSelected(index);
    }
  };

  return (
    <md-tabs>
      <Tab
        text="Upload files"
        icon="upload"
        onClick={() => handleTabSelected("upload")}
      />
      <Tab
        text="Uploads in progresss"
        icon="schedule"
        badge={inProgressUploadsCount}
        onClick={() => {
          handleTabSelected("uploads-in-progress");
          resetInProgressUploadsCount();
        }}
      />
      <Tab
        text="Finished uploads"
        icon="check_circle"
        badge={finishedUploadsCount}
        onClick={() => {
          handleTabSelected("finished-uploads");
          resetFinishedUploadsCount();
        }}
      />
    </md-tabs>
  );
}

function Tab({ text, icon, onClick, badge }) {
  return (
    <md-primary-tab onClick={onClick}>
      {badge > 0 ? (
        <span className={styles.badge}>{badge > 99 ? "99+" : badge}</span>
      ) : (
        ""
      )}
      <md-icon>{icon}</md-icon>
      {text}
    </md-primary-tab>
  );
}
