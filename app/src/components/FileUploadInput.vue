<template>
  <v-file-upload
    browse-text="Local Filesystem"
    divider-text="or choose locally"
    :icon="mdiTrayArrowUp"
    title="Drag and Drop Here"
  />
</template>

<script setup>
import { mdiTrayArrowUp } from '@mdi/js'
</script>

<style scoped></style>
