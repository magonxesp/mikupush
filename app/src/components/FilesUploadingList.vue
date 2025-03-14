<template>
  <md-list>
    <FileUploadingListItem
      v-for="(item, index) in items"
      :key="index"
      :item="item"
      @cancel="emit('cancel', $event)"
      @retry="emit('retry', $event)"
    />
    <md-divider />
  </md-list>
</template>

<script setup lang="ts">
import { BehaviorSubject } from 'rxjs'
import { UploadProgress } from '../model/upload-progress'
import FileUploadingListItem from './FileUploadingListItem.vue'

interface Props {
  items: BehaviorSubject<UploadProgress>[]
}

interface Events {
  retry: [value: UploadProgress],
  cancel: [value: UploadProgress]
}

defineProps<Props>()
const emit = defineEmits<Events>()
</script>

<style scoped>
</style>
