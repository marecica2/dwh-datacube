DROP TABLE IF EXISTS zip_code_location;

CREATE TABLE zip_code_location
(
    id                   BIGSERIAL PRIMARY KEY NOT NULL,
    zip_code             VARCHAR(20),
    latitude             float8,
    longitude            float8
);