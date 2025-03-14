<template>
  <FilesUploadingList
    :items="store.uploadsInProgress"
    @retry="handleRetry"
    @cancel="handleCancel"
  />
</template>

<script setup lang="ts">
import { watch } from 'vue'
import { useAppStore } from '../store'
import FilesUploadingList from './FilesUploadingList.vue'
import { requestUploadForFile } from '../service/upload'
import { UploadProgress } from '../model/upload-progress'

interface Events {
  count: [value: number]
}

const emit = defineEmits<Events>()

const store = useAppStore()
let lastUploadsInProgress = store.uploadsInProgress.length

watch(store.uploadsInProgress, () => {
  const newInProgressUploads = store.uploadsInProgress.length - lastUploadsInProgress
  lastUploadsInProgress = store.uploadsInProgress.length

  emit('count', newInProgressUploads)
})

async function handleRetry (event: UploadProgress) {
  const subject = await requestUploadForFile(event.file)
  store.removeUploadProgress(event)
  store.addUploadProgress(subject)
}

function handleCancel (event: UploadProgress) {
  store.removeUploadProgress(event)
}
</script>
