DROP TABLE IF EXISTS mapping_preset;
CREATE TABLE mapping_preset
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    name        VARCHAR(255) UNIQUE   NOT NULL,
    mapping     JSONB,
    project_id  VARCHAR(10)
);
