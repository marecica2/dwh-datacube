DROP TABLE IF EXISTS service_type_taxonomy;
CREATE TABLE service_type_taxonomy
(
    id                          VARCHAR(20) PRIMARY KEY NOT NULL,
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
    supplier_service_type            VARCHAR(50),
    standard_service_type_letter     VARCHAR(50),
    standard_service_type_parcel     VARCHAR(50),
    standard_service_type_letter_key VARCHAR(50),
    standard_service_type_parcel_key VARCHAR(50)
);

DROP TABLE IF EXISTS standard_rate_card;
CREATE TABLE standard_rate_card
(
    id                    BIGSERIAL PRIMARY KEY NOT NULL,
    supplier_name         VARCHAR(50),
    supplier_service_type VARCHAR(50),
    supplier_zone         VARCHAR(50),
    weight                FLOAT,
    weight_type           VARCHAR(50),
    weight_from           FLOAT,
    weight_to             FLOAT,
    price                 DECIMAL,
    price_per_unit        DECIMAL
);