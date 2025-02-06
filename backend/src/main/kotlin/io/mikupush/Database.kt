package io.mikupush

import io.ktor.server.application.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun Application.configureDatabase() {
    Database.connect(
        url = "jdbc:postgresql://$postgreSqlHost:$postgreSqlPort/$postgreSqlDatabase",
        driver = "org.postgresql.Driver",
        user = postgreSqlUser,
        password = postgreSqlPassword
    )

    transaction {
        SchemaUtils.create(UploadsTable)
    }
}

object UploadsTable : LongIdTable("uploads") {
    val uuid = uuid("uuid")
    val fileName = varchar("name", 255)
    val fileMimeType = varchar("mime_type", 128)
    val fileSize = long("size")
    val uploadedAt = timestamp("uploaded_at")
}

@Serializable
data class UploadDetails(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val fileName: String,
    val fileMimeType: String,
    val fileSizeBytes: Long,
    val uploadedAt: Instant
)

fun findById(fileId: UUID) = transaction {
    UploadsTable.selectAll()
        .where { UploadsTable.uuid eq fileId }
        .limit(1)
        .firstOrNull()
        ?.toDto()
}

fun UploadDetails.insertOrUpdate() {
    val upload = this

    transaction {
        val existing = UploadsTable.selectAll()
            .where { UploadsTable.uuid eq upload.id }
            .limit(1)
            .firstOrNull()

        if (existing != null) {
            upload.update()
        } else {
            upload.insert()
        }
    }
}

fun UploadDetails.insert() {
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

fun UploadDetails.update() {
    val upload = this

    transaction {
        UploadsTable.update(where = { UploadsTable.uuid eq upload.id }) {
            it[fileName] = upload.fileName
            it[fileMimeType] = upload.fileMimeType
            it[fileSize] = upload.fileSizeBytes
            it[uploadedAt] = Clock.System.now()
        }
    }
}

fun UploadDetails.delete() {
    val upload = this

    transaction {
        UploadsTable.deleteWhere { UploadsTable.uuid eq upload.id }
    }
}

private fun ResultRow.toDto() = UploadDetails(
    id = this[UploadsTable.uuid],
    fileName = this[UploadsTable.fileName],
    fileMimeType = this[UploadsTable.fileMimeType],
    fileSizeBytes = this[UploadsTable.fileSize],
    uploadedAt = this[UploadsTable.uploadedAt]
)