<template>
  <md-list>
    <FileUploadingListItem
      name="pekora.png"
      mime-type="image/jpeg"
      :speed="100"
      :progress="0.5"
    />
    <md-divider />
  </md-list>
</template>

<script setup lang="ts">
import FileUploadingListItem from './FileUploadingListItem.vue'
</script>

<style scoped>
</style>
