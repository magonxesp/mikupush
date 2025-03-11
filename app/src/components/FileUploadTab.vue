<template>
    <FileUploadInput @change="handleSelectedFiles" />
</template>

<script setup>
import FileUploadInput from './FileUploadInput.vue'
import { useAppStore } from "../store.js";

const store = useAppStore();

/**
 * Selected files handler
 *
 * @param {File[]} files
 */
function handleSelectedFiles(files) {
  store.upload(files)
}
</script>