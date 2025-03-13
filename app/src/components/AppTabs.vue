<template>
  <div class="app-tabs">
    <md-tabs>
      <md-primary-tab
        id="upload-tab"
        aria-controls="upload-panel"
        @click="currentTab = 0"
      >
        <md-icon>upload</md-icon>
        Upload files
      </md-primary-tab>
      <md-primary-tab
        id="uploads-in-progress-tab"
        aria-controls="uploads-in-progress-panel"
        @click="currentTab = 1"
      >
        <md-icon>schedule</md-icon>
        Uploads in progress
      </md-primary-tab>
      <md-primary-tab
        id="finished-uploads-tab"
        aria-controls="finished-uploads-panel"
        @click="currentTab = 2"
      >
        <md-icon>check_circle</md-icon>
        Finished uploads
      </md-primary-tab>
    </md-tabs>
    <div
      v-for="(tab, index) in tabs"
      :id="`${tab[0]}-panel`"
      :key="index"
      role="tabpanel"
      :aria-labelledby="`${tab[0]}-tab`"
      :hidden="currentTab != index"
    >
      <component :is="tab[1]" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import FileUploadTab from './FileUploadTab.vue'
import FileUploadsTab from './FileUploadsTab.vue'
import FileUploadsFinishedTab from './FileUploadsFinishedTab.vue'

const tabs = [
  ['upload', FileUploadTab],
  ['uploads-in-progress', FileUploadsTab],
  ['finished-uploads', FileUploadsFinishedTab]
]

const currentTab = ref(0)
</script>

<style scoped>
.tab-title {
  margin-top: 0.8em;
}

.app-tabs {
  display: flex;
  flex-direction: column;
  height: 100%;
}

[role="tabpanel"] {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

[hidden] {
  display: none !important;
}
</style>
