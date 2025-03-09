<script setup>
import AppTabs from './components/AppTabs.vue';
import AppTitle from './components/AppTitle.vue'
import FileUploadInput from './components/FileUploadInput.vue';
</script>

<template>
  <div class="drag-area"></div>
  <v-app>
    <v-container>
      <AppTitle />
      <AppTabs />
      <FileUploadInput />
    </v-container>
  </v-app>
</template>

<style scoped>
.drag-area {
  width: 100%;
  background-color: red;
  height: 2rem;
  -webkit-app-region: drag;
}
</style>
