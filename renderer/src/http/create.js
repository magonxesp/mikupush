import { axiosInstance } from './client'

export class FileCreateRequest {
  #uuid
  #name
  #mimeType
  #size

  /**
   * Constructor.
   * @param {object} param0
   * @param {string} param0.uuid
   * @param {string} param0.name
   * @param {string} param0.mimeType
   * @param {number} param0.size
   */
  constructor({ uuid, name, mimeType, size }) {
    this.#uuid = uuid;
    this.#name = name;
    this.#mimeType = mimeType;
    this.#size = size;
  }

  toJson() {
    return JSON.stringify({
      'uuid': this.#uuid,
      'name': this.#name,
      'mime_type': this.#mimeType,
      'size': this.#size
    })
  }
}

export function create() {

}