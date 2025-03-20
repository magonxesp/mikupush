import { useContext } from "react";
import FileIcon from "../FileIcon/FileIcon";
import { UploadsContext } from "../../context/upload";
import styles from "./UploadsProgressTab.module.css";

export default function UploadsProgressTab() {
  const { inProgressUploads } = useContext(UploadsContext);

  if (inProgressUploads.length > 0) {
    return <UploadsProgressList items={inProgressUploads} />
  } else {
    return <EmptyState />
  }
}

function UploadsProgressList({ items }) {
  return (
    <md-list className={styles.list}>
      {items.map((upload, index) => (
        <UploadProgressItemWithDivider
          key={index}
          upload={upload}
          index={index}
          totalItems={items.length}
        />
      ))}
    </md-list>
  );
}

function UploadProgressItemWithDivider({ index, upload, totalItems }) {
  const { cancelUpload, retryUpload } = useContext(UploadsContext);

  return (
    <>
      <UploadProgressItem
        upload={upload}
        onRetry={() => retryUpload(upload)}
        onCancel={() => cancelUpload(upload)}
      />
      {index < totalItems - 1 ? (
        <md-divider key={`divider-${index}`} />
      ) : (
        ""
      )}
    </>
  );
}

function UploadProgressItem({ upload, onRetry, onCancel }) {
  return (
    <md-list-item>
      <div slot="start">
        <FileIcon mimeType={upload.mimeType} />
      </div>

      {!upload.finished ? (
        <InProgress upload={upload} onCancel={onCancel} />
      ) : (
        ""
      )}

      {upload.finished && upload.error !== "" ? (
        <Error upload={upload} onRetry={onRetry} onCancel={onCancel} />
      ) : (
        ""
      )}
    </md-list-item>
  );
}

function InProgress({ upload, onCancel }) {
  const formatSpeed = (speed) => {
    if (typeof speed === "undefined" || speed <= 0) {
      return `0 B/s`;
    }

    const kb = speed / 1024;
    const mb = kb / 1024;
    const gb = mb / 1024;

    if (gb >= 1) return `${gb.toFixed(0)} GB/s`;
    if (mb >= 1) return `${mb.toFixed(0)} MB/s`;
    if (kb >= 1) return `${kb.toFixed(0)} KB/s`;
    return `${speed.toFixed(0)} B/s`;
  };

  return (
    <>
      <div slot="headline" className={styles.name}>
        {upload.name}
      </div>
      <div slot="supporting-text" className={styles.speed}>
        {formatSpeed(upload.speed)}
      </div>
      <div slot="end" className={styles.actions}>
        <md-circular-progress
          className={styles.progress}
          value={upload.progress}
        />
        <md-icon-button onClick={onCancel}>
          <md-icon className={styles.cancel}>close</md-icon>
        </md-icon-button>
      </div>
    </>
  );
}

function Error({ upload, onRetry, onCancel }) {
  return (
    <>
      <div slot="headline" className={styles.name}>
        {upload.name}
      </div>
      <div slot="supporting-text" className={styles.error}>
        {upload.error}
      </div>
      <div slot="end" className={styles.actions}>
        <md-icon-button onClick={onRetry}>
          <md-icon className={styles.retry}>refresh</md-icon>
        </md-icon-button>
        <md-icon-button onClick={onCancel}>
          <md-icon className={styles.cancel}>close</md-icon>
        </md-icon-button>
      </div>
    </>
  );
}

function EmptyState() {
  return (
    <div className={styles.emptyState}>
      <md-icon>inventory_2</md-icon>
      <p className="md-typescale-body-large">No uploads in progress yet. Try uploading a file!</p>
    </div>
  )
}