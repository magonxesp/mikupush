package io.mikupush

import io.mikupush.upload.UploadsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun setupDatabase() {
    Database.connect(
        "jdbc:sqlite:$appDataDir/data.db",
        "org.sqlite.JDBC"
    )

    transaction {
        SchemaUtils.create(UploadsTable)
    }
}
