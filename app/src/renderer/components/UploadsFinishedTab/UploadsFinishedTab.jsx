import { useContext } from "react";
import FileIcon from "../FileIcon/FileIcon";
import { UploadsContext } from "../../context/upload";
import styles from "./UploadsFinishedTab.module.css";
import { writeToClipboard } from '../../ipc/clipboard'
import { showNotification } from "../../ipc/notification";

export default function UploadsFinishedTab() {
  const { finishedUploads } = useContext(UploadsContext);

  if (finishedUploads.length > 0) {
    return <FinishedUploadList items={finishedUploads} />
  } else {
    return <EmptyState />
  }
}

function FinishedUploadList({ items }) {
  return (
    <md-list className={styles.list}>
      {items.map((upload, index) => (
        <UploadItemWithDivider
          key={index}
          upload={upload}
          index={index}
          totalItems={items.length}
        />
      ))}
    </md-list>
  );
}

function UploadItemWithDivider({ index, upload, totalItems }) {
  return (
    <>
      <UploadItem upload={upload} />
      {index < totalItems - 1 ? (
        <md-divider key={`divider-${index}`} />
      ) : (
        ""
      )}
    </>
  );
}

function UploadItem({ upload }) {
  const { deleteUpload } = useContext(UploadsContext);

  const handleCopyLink = () => {
    writeToClipboard(`http://localhost:8080/u/${upload.id}`)

    showNotification({
      title: '📎Link copied to clipboard!',
      body: `The link for ${upload.name} is in the clipboard`
    })
  }

  const handleDelete = () => {
    deleteUpload(upload.id);
  }

  return (
    <md-list-item>
      <div slot="start">
        <FileIcon mimeType={upload.mimeType} />
      </div>

      <div slot="headline" className={styles.name}>
        {upload.name}
      </div>
      <div slot="supporting-text" className={styles.uploadedAt}>
        {''}
      </div>
      <div slot="end" className={styles.actions}>
        <md-icon-button onClick={handleCopyLink} className={styles.copyLink}>
          <md-icon>link</md-icon>
        </md-icon-button>
        <md-icon-button onClick={handleDelete}>
          <md-icon className={styles.cancel}>delete</md-icon>
        </md-icon-button>
      </div>
    </md-list-item>
  );
}

function EmptyState() {
  return (
    <div className={styles.emptyState}>
      <md-icon>inventory_2</md-icon>
      <p className="md-typescale-body-large">No files uploaded yet. Try uploading one!</p>
    </div>
  )
}