import { defineStore } from 'pinia'
import { Ref, ref } from 'vue'
import { UploadProgress } from './model/upload-progress'

export const useAppStore = defineStore('app', () => {
    const uploadsInProgress: Ref<UploadProgress[]> = ref([])

    return {
        uploadsInProgress,
    }
})