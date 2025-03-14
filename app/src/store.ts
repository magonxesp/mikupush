import { defineStore } from 'pinia'
import { UploadProgress } from './model/upload-progress'
import { Ref, ref } from 'vue'
import { BehaviorSubject } from 'rxjs'

export const useAppStore = defineStore('app', () => {
  const uploadsInProgress: Ref<BehaviorSubject<UploadProgress>[]> = ref([])

  function addUploadProgress (subject: BehaviorSubject<UploadProgress>) {
    console.log('add new upload')
    uploadsInProgress.value.push(subject)
  }

  function removeUploadProgress (progress: UploadProgress) {
    console.log('remove upload')
    uploadsInProgress.value = uploadsInProgress.value.filter((item) => {
      const value = item.getValue()
      return value.details.id !== progress.details.id
    })
  }

  return {
    uploadsInProgress,
    addUploadProgress,
    removeUploadProgress
  }
})
