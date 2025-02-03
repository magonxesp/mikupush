package io.mikupush.uploads

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction

object UploadsTable : LongIdTable("uploads") {
    val uuid = uuid("uuid")
    val fileName = varchar("name", 255)
    val fileMimeType = varchar("mime_type", 128)
    val fileSize = long("size")
    val uploadedAt = timestamp("uploaded_at")
}

fun Upload.insert() {
    val upload = this

    transaction {
        UploadsTable.insert {
            it[uuid] = upload.id
            it[fileName] = upload.fileName
            it[fileMimeType] = upload.fileMimeType
            it[fileSize] = upload.fileSizeBytes
            it[uploadedAt] = Clock.System.now()
        }
    }
}

fun findAllUploads() = transaction {
    UploadsTable.selectAll()
        .orderBy(UploadsTable.uploadedAt, SortOrder.DESC)
        .map { it.toDto() }
}

private fun ResultRow.toDto() = Upload(
    id = this[UploadsTable.uuid],
    fileName = this[UploadsTable.fileName],
    fileMimeType = this[UploadsTable.fileMimeType],
    fileSizeBytes = this[UploadsTable.fileSize],
    uploadedAt = this[UploadsTable.uploadedAt]
)