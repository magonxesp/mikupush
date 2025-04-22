import { fileTypeFromBlob } from 'file-type'

export async function resolveMimeType(file: File) {
    if (typeof file.type !== 'undefined' && file.type !== '') {
        return file.type
    }

    const blobType = await fileTypeFromBlob(file)
    if (typeof blobType !== 'undefined') {
        return blobType.mime
    }

    return 'application/octet-stream'
}
