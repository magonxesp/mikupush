<template>
  <FileUploadInput @change="handleSelectedFiles" />
</template>

<script setup lang="ts">
import FileUploadInput from './FileUploadInput.vue'
import { useAppStore } from '../store.js'
import { requestUploadForFile } from '../helpers/upload'

const store = useAppStore()

function handleSelectedFiles (files: File[]) {
  for (const file of files) {
    requestUploadForFile(file).then(progress => {
      store.uploadsInProgress.push(progress)
    })
  }
}
</script>
