DROP TABLE IF EXISTS zip_code_location;
CREATE TABLE zip_code_location
(
    id                   BIGSERIAL PRIMARY KEY NOT NULL,
    zip_code             VARCHAR(20),
    latitude             float8,
    longitude            float8
);

DROP TABLE IF EXISTS service_type_taxonomy;
CREATE TABLE service_type_taxonomy
(
    id                          VARCHAR(20) PRIMARY KEY NOT NULL,
    name                        VARCHAR(255),
    expedite                    BOOLEAN,
    level_down_letter_key       VARCHAR(50),
    level_down_parcel_key       VARCHAR(50),
    standard_service_type_group VARCHAR(10)
);

DROP TABLE IF EXISTS service_type_mapping;
CREATE TABLE service_type_mapping
(
    id                               BIGSERIAL PRIMARY KEY NOT NULL,
    supplier_name                    VARCHAR(50),
    supplier_service_type            VARCHAR(250),
    standard_service_type_letter     VARCHAR(250),
    standard_service_type_parcel     VARCHAR(250),
    standard_service_type_letter_key VARCHAR(50),
    standard_service_type_parcel_key VARCHAR(50)
);

DROP TABLE IF EXISTS standard_rate_card;
CREATE TABLE standard_rate_card
(
    id                    BIGSERIAL PRIMARY KEY NOT NULL,
    supplier_name         VARCHAR(50),
    supplier_service_type VARCHAR(250),
    supplier_zone         VARCHAR(50),
    weight                FLOAT,
    weight_type           VARCHAR(50),
    weight_from           FLOAT,
    weight_to             FLOAT,
    price                 DECIMAL,
    price_per_unit        DECIMAL
);