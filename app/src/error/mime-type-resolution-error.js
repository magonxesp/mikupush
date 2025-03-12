export class MimeTypeResolutionError extends Error {
    /**
     * Build MimeTypeResolutionError from File
     *
     * @param {File} file
     */
    static forFile(file) {
        return new MimeTypeResolutionError(`Unable to resolve mime type of file ${file.name}`)
    }
}