import { fileTypeFromBuffer } from 'file-type'
import { v4 as uuidv4 } from 'uuid'
import { MimeTypeResolutionError } from '../error/mime-type-resolution-error'

export class FileDetails {
  readonly id: string
  readonly name: string
  readonly size: number
  readonly mimeType: string

  constructor (name: string, size: number, mimeType: string) {
    this.id = uuidv4()
    this.name = name
    this.size = size
    this.mimeType = mimeType
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

    return new FileDetails(
      file.name,
      file.size,
      mimeType
    )
  }
}
