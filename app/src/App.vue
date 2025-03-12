<script setup>
import AppTabs from './components/AppTabs.vue'
import AppTitle from './components/AppTitle.vue'
</script>

<template>
  <div class="drag-area" />
  <AppTitle />
  <AppTabs />
</template>

<style scoped>
.drag-area {
  width: 100%;
  background-color: red;
  height: 2rem;
  -webkit-app-region: drag;
}
</style>
