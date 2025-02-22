CREATE TABLE uploaded_files (
    _id BIGSERIAL NOT NULL PRIMARY KEY,
    uuid UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(128) NOT NULL,
    size BIGINT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX uploaded_files_uuid ON uploaded_files(uuid);