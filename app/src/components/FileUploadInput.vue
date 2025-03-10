<template>
  <div 
    class="area"
    :class="{'active': highlight}"
    @click="openFileDialog"
    @dragover="showHighlight"
    @dragenter="showHighlight"
    @dragleave="hideHighlight"
    @drop.prevent="handleDroppedFiles"
  >
    <md-icon>upload</md-icon>
    <p class="md-typescale-body-large">Drop your file here to upload it, or click to select a file.</p>
    <input type="file" hidden @change="handleSelectedFiles" ref="file-input" />
  </div>
</template>

<script setup>
import { onMounted, ref, useTemplateRef } from 'vue'

const fileInput = useTemplateRef('file-input')
const highlight = ref(false)

function openFileDialog() {
  fileInput.value.click()
}

function showHighlight(event) {
  event.preventDefault();
  event.stopPropagation();
  highlight.value = true
}

function hideHighlight(event) {
  event.preventDefault();
  event.stopPropagation();
  highlight.value = false
}

function handleSelectedFiles(event) {
  console.log(event.target.files)
  // TODO: dispatch event with the selected files
}

function handleDroppedFiles(event) {
  event.preventDefault();
  event.stopPropagation();
  console.log(event.dataTransfer.files)
  // TODO: check is only file
  // TODO: dispatch event with the selected files
}
</script>

<style scoped>
.area {
  border-radius: 2em;
  border-style: dashed;
  border-width: 0.3em;
  border-color: var(--foreground-color);
  flex-grow: 1;
  margin: 2em;
  padding: 1em;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.area:hover, .active {
  border-color: var(--primary-color);
  color: var(--primary-color);
}

md-icon {
  --md-icon-size: 8em;
  width: 100%;
  height: auto;
}

p {
  text-align: center;
}
</style>
