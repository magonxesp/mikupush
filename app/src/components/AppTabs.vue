<template>
  <md-tabs>
    <md-primary-tab>
        <md-icon slot="icon">upload</md-icon>
        Upload files
    </md-primary-tab>
    <md-primary-tab>
        <md-icon slot="icon">schedule</md-icon>
        Uploads in progress
    </md-primary-tab>
    <md-primary-tab>
        <md-icon slot="icon">check_circle</md-icon>
        Finished uploads
    </md-primary-tab>
  </md-tabs>
</template>

<script setup>
import { mdiTrayArrowUp } from '@mdi/js';
import { mdiClockOutline } from '@mdi/js';
import { mdiFormatListBulleted } from '@mdi/js';
</script>

<style scoped>
.tab-title {
    margin-top: 0.8em;
}
</style>