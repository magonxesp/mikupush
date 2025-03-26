CREATE TABLE file_uploads (
    _id BIGSERIAL NOT NULL PRIMARY KEY,
    uuid UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(128) NOT NULL,
    size BIGINT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX file_uploads_uuid ON file_uploads(uuid);