import { resolveMimeType } from "../helpers/mime-type"
import { v4 as uuidv4 } from "uuid"

export class Upload {
    #id
    #name
    #size
    #mimeType
    #uploadedAt

    /**
     * Constructor.
     * @param {object} param0 
     * @param {string} param0.id
     * @param {string} param0.name
     * @param {number} param0.size
     * @param {string} param0.mimeType
     * @param {Date} param0.uploadedAt
     */
    constructor({ id, name, size, mimeType, uploadedAt }) {
        this.#id = id
        this.#name = name
        this.#size = size
        this.#mimeType = mimeType
        this.#uploadedAt = uploadedAt
    }

    get id() {
        return this.#id
    }

    get name() {
        return this.#name
    }

    get size() {
        return this.#size
    }

    get uploadedAt() {
        return this.#uploadedAt
    }

    get mimeType() {
        return this.#mimeType
    }

    static async fromFile(file) {
        return new Upload({
            id: uuidv4(),
            name: file.name,
            size: file.size,
            mimeType: await resolveMimeType(file),
            uploadedAt: new Date()
        })
    }
}