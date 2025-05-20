import { useEffect, useState } from 'react'
import styles from './App.module.css'
import AppTabs from './components/AppTabs/AppTabs'
import AppTitle from './components/AppTitle/AppTitle'
import InputTab from './components/InputTab/InputTab'
import UploadsFinishedTab from './components/UploadsFinishedTab/UploadsFinishedTab'
import UploadsProgressTab from './components/UploadsProgressTab/UploadsProgressTab'
import { UploadsContext } from './context/upload'
import { SerializableUploadRequest, UploadRequest } from '../shared/model/upload-request.ts'
import { Upload } from '../shared/model/upload.ts'

function App() {
	const tabs = {
		upload: <InputTab />,
		'uploads-in-progress': <UploadsProgressTab />,
		'finished-uploads': <UploadsFinishedTab />,
	}

	const uploadChannels = window.uploadChannels
	const systemChannels = window.systemChannels
	const [currentTab, setCurrentTab] = useState<keyof typeof tabs>('upload')
	const [inProgressUploads, setInProgressUploads] = useState<UploadRequest[]>([])
	const [finishedUploads, setFinishedUploads] = useState<Upload[]>([])
	const [inProgressUploadsCount, setInProgressUploadsCount] = useState(0)
	const [finishedUploadsCount, setFinishedUploadsCount] = useState(0)

	useEffect(() => {
		uploadChannels.findAll().then(uploads => {
			setFinishedUploads(uploads.map(upload => Upload.fromSerializable(upload)))
		})
	}, [])

	const moveRequestAsFinished = (request: UploadRequest) => {
		setInProgressUploads((previous) =>
			previous.filter((item) => item.id !== request.id)
		)

		setInProgressUploadsCount((count) => count > 0 ? count - 1 : count)
		setFinishedUploads((previous) => [request.upload, ...previous])

		if (currentTab !== 'finished-uploads') {
			setFinishedUploadsCount((count) => count + 1)
		}

		systemChannels.showNotification({
			title: `The file ${request.name} has been uploaded!`,
			body: 'Now, you can grab the link for share it!'
		})
	}

	const handleProgressUpdate = (serializable: SerializableUploadRequest) => {
		const request = UploadRequest.fromSerializable(serializable)

		if (request.finishedSuccess) {
			moveRequestAsFinished(request)
		} else {
			setInProgressUploads((previous) =>
				previous.map((item) => item.id === request.id ? request : item)
			)
		}
	}

	const requestUploads = async (files: File[]) => {
		let newUploads: UploadRequest[] = []

		try {
			const filePaths = files.map((file) => systemChannels.resolveWebFilePath(file))

			newUploads = await uploadChannels.enqueue(filePaths)
				.then(requests => requests.map(request => UploadRequest.fromSerializable(request)))

			uploadChannels.onUploadProgress(handleProgressUpdate)
		} catch (exception) {
			console.error('unable to upload file', exception)
		}

		setInProgressUploads((previous) => [...previous, ...newUploads])

		if (currentTab !== 'uploads-in-progress') {
			setInProgressUploadsCount((previous) => previous + newUploads.length)
		}
	}

	const cancelUpload = (request: UploadRequest) => {
		uploadChannels.abort(request.id)
		setInProgressUploads(inProgressUploads.filter((item) => item.id !== request.id))
	}

	const retryUpload = (request: UploadRequest) => {
		uploadChannels.retry(request.toSerializable())
		uploadChannels.onUploadProgress(handleProgressUpdate)
	}

	const resetInProgressUploadsCount = () => setInProgressUploadsCount(0)
	const resetFinishedUploadsCount = () => setFinishedUploadsCount(0)

	const handleTabSelected = (index: keyof typeof tabs) => {
		setCurrentTab(index)
	}

	const deleteUpload = async (id: string) => {
		await uploadChannels.delete(id)

		setFinishedUploads((previous) =>
			previous.filter((item) => item.id !== id)
		)
	}

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
	)
}

export default App
