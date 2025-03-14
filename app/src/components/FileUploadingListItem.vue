<template>
  <md-list-item>
    <div slot="start">
      <FileIcon mime-type="image/png" />
    </div>
    <div
      slot="headline"
      class="name"
    >
      {{ progress.details.name }}
    </div>
    <div
      v-if="progress.isInProgress"
      slot="supporting-text"
      class="speed"
    >
      {{ formatSpeed(progress.speed) }}
    </div>
    <div
      v-if="progress.isFinishedFailed"
      slot="supporting-text"
      class="error"
    >
      {{ progress.error }}
    </div>
    <div
      v-if="progress.isFinishedFailed"
      slot="end"
    >
      <md-icon-button @click="handleRetry">
        <md-icon class="retry">
          refresh
        </md-icon>
      </md-icon-button>
    </div>
    <div
      v-if="progress.isInProgress"
      slot="end"
      class="actions"
    >
      <md-circular-progress
        class="progress"
        :value="props.progress"
      />
      <md-icon-button @click="handleCancel">
        <md-icon class="cancel">
          close
        </md-icon>
      </md-icon-button>
    </div>
  </md-list-item>
</template>

<script setup lang="ts">
import { defineEmits, defineProps } from 'vue'
import FileIcon from './FileIcon.vue'
import {UploadProgress} from "../model/upload-progress";

interface Props {
  progress: UploadProgress
}

const props = defineProps<Props>()

const emit = defineEmits(['cancel', 'retry'])

function handleCancel () {
  emit('cancel')
}

function handleRetry () {
  emit('retry')
}

function formatSpeed (speed: number) {
  const kb = speed / 1024
  const mb = kb / 1024
  const gb = mb / 1024

  if (gb >= 1) return `${gb.toFixed(0)} GB/s`
  if (mb >= 1) return `${mb.toFixed(0)} MB/s`
  if (kb >= 1) return `${kb.toFixed(0)} KB/s`
  return `${speed.toFixed(0)} B/s`
}
</script>

<style scoped>
.speed, .retry {
  color: var(--foreground-color);
}

.cancel, .error {
  color: red;
}

.speed, .error, .name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.actions {
  display: flex;
  justify-content: center;
  align-items: center;
}

.progress {
  margin-right: 1em;
}
</style>
