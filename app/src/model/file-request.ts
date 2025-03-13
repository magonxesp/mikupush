import { v4 as uuidv4 } from 'uuid'
import { fileTypeFromBuffer } from 'file-type'
import { MimeTypeResolutionError } from '../error/mime-type-resolution-error'

export class FileRequest {
  readonly id: string
  readonly name: string
  readonly size: number
  readonly mimeType: string
  readonly bytes: Uint8Array

  constructor (name: string, size: number, mimeType: string, bytes: Uint8Array) {
    this.id = uuidv4()
    this.name = name
    this.size = size
    this.mimeType = mimeType
    this.bytes = bytes
  }

  static async fromFile (file: File) {
    let mimeType = file.type
    const bytes = await file.bytes()

    if (!mimeType) {
      const result = await fileTypeFromBuffer(bytes)
      if (typeof result === 'undefined') {
        throw MimeTypeResolutionError.forFile(file)
      }

      mimeType = result.mime
    }

    return new FileRequest(
      file.name,
      file.size,
      mimeType,
      bytes
    )
  }
}
