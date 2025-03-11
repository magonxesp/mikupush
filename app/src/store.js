import { defineStore } from 'pinia'
import {ref} from "vue";

export const useAppStore = defineStore('app', () => {
    const uploadQueue = ref([])

    /**
     * Add to upload queue the files and run upload task
     *
     * @param {File[]} files
     */
    function upload(files) {
        uploadQueue.value.push(files)
    }

    return {
        uploadQueue,
        upload
    }
})