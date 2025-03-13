<template>
  <FileUploadInput @change="handleSelectedFiles" />
</template>

<script setup lang="ts">
import FileUploadInput from './FileUploadInput.vue'
import { useAppStore } from '../store.js'
import { FileRequest } from '../model/file-request'

const store = useAppStore()

function handleSelectedFiles (files: FileRequest[]) {
  store.upload(files)
}
</script>
