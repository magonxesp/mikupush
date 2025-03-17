import { useContext, useState, useRef } from "react";
import styles from "./InputTab.module.css";
import { UploadsContext } from "../../context";

export default function InputTab() {
  const [active, setActive] = useState(false);
  const fileInputRef = useRef(null);
  const { requestUploads } = useContext(UploadsContext);

  const openFileDialog = () => {
    fileInputRef.current.click();
  };

  const handleDragOver = (event) => {
    event.preventDefault();
    event.stopPropagation();

    setActive(true);
  };

  const handleDragLeave = (event) => {
    event.preventDefault();
    event.stopPropagation();

    setActive(false);
  };

  const handleDrop = (event) => {
    event.preventDefault();
    event.stopPropagation();

    const isFile = (item) => {
      if (item.kind !== "file") {
        return false;
      }

      const entry = item.webkitGetAsEntry();
      return entry.isFile;
    };

    const files = Array.from(event.dataTransfer.items)
      .filter(isFile)
      .map((item) => item.getAsFile());

    requestUploads(files);
    setActive(false);
  };

  const handleSelectedFiles = (event) => {
    event.preventDefault();
    event.stopPropagation();

    requestUploads(event.target.files);
    setActive(false);
  };

  return (
    <div
      className={`${styles.area} ${active ? styles.active : ""}`}
      onClick={openFileDialog}
      onDragOver={handleDragOver}
      onDragEnter={handleDragOver}
      onDragLeave={handleDragLeave}
      onDrop={handleDrop}
    >
      <md-icon>upload</md-icon>
      <p className="md-typescale-body-large">
        Drop your file here to upload it, or click to select a file.
      </p>
      <input
        type="file"
        hidden
        onChange={handleSelectedFiles}
        ref={fileInputRef}
      />
    </div>
  );
}
