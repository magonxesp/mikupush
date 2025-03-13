import { defineStore } from 'pinia'
import { Ref, ref } from 'vue'
import { FileRequest } from './model/file-request'

export const useAppStore = defineStore('app', () => {
    const uploadQueue: Ref<FileRequest[]> = ref([])

    function upload(requests: FileRequest[]) {
        console.log(requests)
        uploadQueue.value.push(...requests)
    }

    async function startUploading() {
        
    }

    return {
        uploadQueue,
        upload
    }
})