package io.mikupush

import io.github.cdimascio.dotenv.dotenv

val dotEnv get() = dotenv {
    ignoreIfMissing = true
}

val postgreSqlUser: String get() = dotEnv["POSTGRESQL_USER"]
val postgreSqlPassword: String get() = dotEnv["POSTGRESQL_PASSWORD"]
val postgreSqlDatabase: String get() = dotEnv["POSTGRESQL_DATABASE"]
val postgreSqlHost: String get() = dotEnv["POSTGRESQL_HOST"]
val postgreSqlPort: String get() = dotEnv["POSTGRESQL_PORT"]