import { v4 as uuidv4 } from 'uuid'
import { fileTypeFromBuffer } from 'file-type'
import { MimeTypeResolutionError } from '../error/mime-type-resolution-error'

export class FileRequest {
  /** @type {string} */
  #id

  /** @type {string} */
  #name

  /** @type {number} */
  #size

  /** @type {string|null} */
  #mimeType

  /** @type {Uint8Array} */
  #bytes

  /**
     * Create a file request
     *
     * @param {string} name
     * @param {number} size
     * @param {string|null} mimeType
     * @param {Uint8Array} bytes
     */
  constructor (name, size, mimeType, bytes) {
    this.#id = uuidv4()
    this.#name = name
    this.#size = size
    this.#mimeType = mimeType
    this.#bytes = bytes
  }

  get id () {
    return this.#id
  }

  get name () {
    return this.#name
  }

  get size () {
    return this.#size
  }

  get mimeType () {
    return this.#mimeType
  }

  get bytes () {
    return this.#bytes
  }

  /**
     * Create from file
     *
     * @param {File} file
     */
  static async fromFile (file) {
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
