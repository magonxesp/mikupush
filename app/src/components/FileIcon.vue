<template>
  <FontAwesomeIcon
    class="icon"
    :icon="icon"
  />
</template>

<script setup>
import { computed, defineProps } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
  faFile,
  faFileZipper,
  faFileCode,
  faFileLines,
  faFilePdf,
  faFilePowerpoint,
  faFileWord,
  faFileExcel,
  faFileVideo,
  faFileAudio,
  faFileImage,
} from '@fortawesome/free-solid-svg-icons'

const props = defineProps({
  mimeType: {
    type: String,
    required: true,
  },
})

const icon = computed(() => resolveIcon(props.mimeType))

/**
 * Resolve the file icon by mime type
 *
 * @param {string} mimeType
 */
function resolveIcon (mimeType) {
  const excelTypes = [
    'application/vnd.ms-excel',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    'application/vnd.oasis.opendocument.spreadsheet',
  ]

  const wordTypes = [
    'application/msword',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'application/rtf',
    'application/vnd.oasis.opendocument.tex',
  ]

  const powerPointTypes = [
    'application/vnd.ms-powerpoint',
    'application/vnd.openxmlformats-officedocument.presentationml.presentation',
    'application/vnd.oasis.opendocument.presentation',
  ]

  const sourceCodeTypes = [
    'text/x-java-source',
    'text/x-c',
    'text/x-c++',
    'text/x-csharp',
    'text/x-python',
    'application/javascript',
    'application/x-typescript',
    'text/html',
    'text/css',
    'application/x-httpd-php',
    'text/x-ruby',
    'text/x-perl',
    'application/x-sh',
    'application/x-powershell',
    'application/sql',
    'text/x-go',
    'text/x-rust',
    'text/x-swift',
    'text/x-kotlin',
    'text/x-lua',
    'text/x-r-source',
    'text/x-matlab',
  ]

  const compressedTypes = [
    'application/zip',
    'application/x-7z-compressed',
    'application/x-rar-compressed',
    'application/gzip',
    'application/x-tar',
    'application/x-bzip2',
    'application/x-xz',
    'application/x-lzma',
    'application/x-apple-diskimage',
  ]

  if (mimeType.startsWith('image/')) {
    return faFileImage
  } else if (mimeType.startsWith('audio/')) {
    return faFileAudio
  } else if (mimeType.startsWith('video/')) {
    return faFileVideo
  } else if (excelTypes.indexOf(mimeType) !== -1) {
    return faFileExcel
  } else if (wordTypes.indexOf(mimeType) !== -1) {
    return faFileWord
  } else if (powerPointTypes.indexOf(mimeType) !== -1) {
    return faFilePowerpoint
  } else if (mimeType === 'application/pdf') {
    return faFilePdf
  } else if (mimeType === 'text/plain') {
    return faFileLines
  } else if (sourceCodeTypes.indexOf(mimeType) !== -1) {
    return faFileCode
  } else if (compressedTypes.indexOf(mimeType) !== -1) {
    return faFileZipper
  } else {
    return faFile
  }
}
</script>

<style scoped>
.icon {
    color: var(--foreground-color);
    font-size: 2.5em;
}
</style>
