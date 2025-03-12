/**
 * The upload queue
 *
 * @type {FileRequest[]}
 */
const uploadQueue = []

/**
 * Handle incoming new upload request
 *
 * @param {FileRequest} request
 */
function handleRequest(request) {
    uploadQueue.push(request)
}

addEventListener('message', handleRequest)