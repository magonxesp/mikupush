import { resolveMimeType } from "../helpers/mime-type"
import { v4 as uuidv4 } from "uuid"

interface UploadObject {
    id: string
    name: string
    size: number
    mimeType: string
    uploadedAt: Date
}

export class Upload {
    private readonly id: string
    private readonly name: string
    private readonly size: number
    private readonly mimeType: string
    private readonly uploadedAt: Date

    constructor({ id, name, size, mimeType, uploadedAt }: UploadObject) {
        this.id = id
        this.name = name
        this.size = size
        this.mimeType = mimeType
        this.uploadedAt = uploadedAt
    }

    toPlainObject(): UploadObject {
        return {
            id: this.id,
            name: this.name,
            size: this.size,
            mimeType: this.mimeType,
            uploadedAt: this.uploadedAt
        }
    }

    static async fromFile(file: File) {
        return new Upload({
            id: uuidv4(),
            name: file.name,
            size: file.size,
            mimeType: await resolveMimeType(file),
            uploadedAt: new Date()
        })
    }
}