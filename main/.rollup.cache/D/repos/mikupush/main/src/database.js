import { DataTypes, Model, Sequelize } from 'sequelize';
import path from 'path';
import { appDataDirectory } from './environment';
export const database = new Sequelize({
    dialect: 'sqlite',
    storage: path.join(appDataDirectory(), 'data.db')
});
const Upload = database.define('Upload', {
    id: {
        type: DataTypes.UUIDV4,
        primaryKey: true,
    },
    name: DataTypes.STRING,
    size: DataTypes.BIGINT,
    mimeType: DataTypes.STRING,
    uploadedAt: DataTypes.DATE,
});
export async function insertUpload(upload) {
    await Upload.create({
        id: upload.id,
        name: upload.name,
        size: upload.size,
        mimeType: upload.mimeType,
        uploadedAt: upload.uploadedAt,
    });
}
export async function findAllUploads() {
    const uploads = await Upload.findAll({
        order: [
            ['uploadedAt', 'DESC']
        ]
    });
    return uploads.map(upload => ({
        id: upload.id,
        name: upload.name,
        size: upload.size,
        mimeType: upload.mimeType,
        uploadedAt: upload.uploadedAt
    }));
}
export async function deleteUpload(uploadId) {
    await Upload.destroy({
        where: {
            id: uploadId
        }
    });
}
//# sourceMappingURL=database.js.map