export class MimeTypeResolutionError extends Error {
  static forFile (file: File) {
    return new MimeTypeResolutionError(`Unable to resolve mime type of file ${file.name}`)
  }
}
