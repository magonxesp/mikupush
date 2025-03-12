import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
    const uploadQueue = ref([])

    /**
     * Add to upload queue the files and run upload task
     *
     * @param {FileRequest[]} requests
     */
    function upload(requests) {
        console.log(requests)
        uploadQueue.value.push(requests)
    }

    return {
        uploadQueue,
        upload
    }
})