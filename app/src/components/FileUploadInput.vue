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
    <p class="md-typescale-body-large">
      Drop your file here to upload it, or click to select a file.
    </p>
    <input
      ref="file-input"
      type="file"
      hidden
      @change="handleSelectedFiles"
    >
  </div>
</template>

<script setup lang="ts">
import { defineEmits, ref, useTemplateRef } from 'vue'
import { FileRequest } from '../model/file-request.js'

type Events = {
  change: [value: FileRequest[]]
}

const emit = defineEmits<Events>()

const fileInput = useTemplateRef('file-input')
const highlight = ref(false)

function openFileDialog () {
  fileInput.value.click()
}

function showHighlight (event: DragEvent) {
  event.preventDefault()
  event.stopPropagation()
  highlight.value = true
}

function hideHighlight (event: DragEvent) {
  event.preventDefault()
  event.stopPropagation()
  highlight.value = false
}

async function handleSelectedFiles (event: Event) {
  const target = event.target as HTMLInputElement

  const files = await Promise.all(
    Array.from(target.files)
      .map(file => FileRequest.fromFile(file))
  )

  emit('change', files)
}

async function handleDroppedFiles (event: DragEvent) {
  event.preventDefault()
  event.stopPropagation()

  const isFile = (item: DataTransferItem) => {
    if (item.kind !== 'file') {
      return false
    }

    const entry = item.webkitGetAsEntry()
    return entry.isFile
  }

  const files = await Promise.all(
    Array.from(event.dataTransfer.items)
      .filter(isFile)
      .map(item => FileRequest.fromFile(item.getAsFile()))
  )

  emit('change', files)
  highlight.value = false
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
