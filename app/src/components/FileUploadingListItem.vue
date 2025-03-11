<template>
    <md-list-item>
        <div slot="start">
            <FileIcon mimeType="image/png" />
        </div>
        <div slot="headline" class="name">{{ props.name }}</div>
        <div 
            v-if="errorMessage == '' || errorMessage == null" 
            class="speed" 
            slot="supporting-text"
        >
            {{ formatSpeed(speed) }}
        </div>
        <div 
            v-if="errorMessage != '' && errorMessage != null"
            class="error" 
            slot="supporting-text"
        >
            {{ errorMessage }}
        </div>
        <div v-if="failed" slot="end">
            <md-icon-button @click="handleRetry">
                <md-icon class="retry">refresh</md-icon>
            </md-icon-button>
        </div>
        <div v-if="!failed" slot="end" class="actions">
            <md-circular-progress class="progress" :value="progress"></md-circular-progress>
            <md-icon-button @click="handleCancel">
                <md-icon class="cancel">close</md-icon>
            </md-icon-button>
        </div>
    </md-list-item>
</template>

<script setup>
import { defineEmits, defineProps, watch, ref } from 'vue';
import FileIcon from './FileIcon.vue';

const props = defineProps({
    name: {
        type: String,
        required: true
    },
    mimeType: {
        type: String,
        required: true
    },
    speed: {
        type: Number,
        default: 0
    },
    progress: {
        type: Number,
        default: 0
    },
    failed: {
        type: Boolean,
        default: false
    },
    errorMessage: {
        type: String,
        default: ''
    }
})

const emit = defineEmits(['cancel', 'retry'])
const speed = ref(props.speed)
const progress = ref(props.progress)

watch(props.speed, (newValue) => {
    speed.value = newValue
})

watch(props.progress, (newValue) => {
    progress.value = newValue
})

function handleCancel() {
    emit('cancel')
}

function handleRetry() {
    emit('retry')
}

/**
 * Get bytes speed text
 * 
 * @param {number} speed 
 */
function formatSpeed(speed) {
    const kb = speed / 1024
    const mb = kb / 1024
    const gb = mb / 1024

    if (gb >= 1) return `${gb.toFixed(0)} GB/s`
    if (mb >= 1) return `${mb.toFixed(0)} MB/s`
    if (kb >= 1) return `${kb.toFixed(0)} KB/s`
    return `${speed.toFixed(0)} B/s`;
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