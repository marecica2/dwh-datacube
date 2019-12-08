DROP TABLE IF EXISTS fact_raw;

CREATE TABLE fact_raw
(
    id                   BIGSERIAL PRIMARY KEY NOT NULL,
    transaction_id       VARCHAR(20),
    supplier_name        VARCHAR(100),
    business_unit        VARCHAR(50),
    origin_city          VARCHAR(50),
    origin_state         VARCHAR(50),
    origin_country       VARCHAR(50),
    origin_zip           VARCHAR(50),
    destination_city     VARCHAR(255),
    destination_state    VARCHAR(255),
    destination_country  VARCHAR(50),
    destination_zip      VARCHAR(50),
    zone                 VARCHAR(50),
    shipment_date        TIMESTAMP,
    delivery_date        TIMESTAMP,
    service_type         VARCHAR(255),
    billable_weight      FLOAT NOT NULL,
    actual_weight        FLOAT,
    length               FLOAT,
    width                FLOAT,
    height               FLOAT,
    cost                 DECIMAL NOT NULL,
    accessorial_service1 VARCHAR(255),
    accessorial_service2 VARCHAR(255),
    accessorial_service3 VARCHAR(255),
    accessorial_charge1  DECIMAL,
    accessorial_charge2  DECIMAL,
    accessorial_charge3  DECIMAL,
    discount             DECIMAL,
    distance             FLOAT,
    processed            BOOLEAN
);

DROP TABLE IF EXISTS fact;

CREATE TABLE fact
(
    id                   BIGSERIAL PRIMARY KEY NOT NULL,
    transaction_id       VARCHAR(20),
    supplier_name        VARCHAR(100),
    business_unit        VARCHAR(50),
    origin_city          VARCHAR(50),
    origin_state         VARCHAR(50),
    origin_country       VARCHAR(50),
    origin_zip           VARCHAR(50),
    destination_city     VARCHAR(255),
    destination_state    VARCHAR(255),
    destination_country  VARCHAR(50),
    destination_zip      VARCHAR(50),
    zone                 VARCHAR(50),
    standard_zone        VARCHAR(50),
    shipment_date        TIMESTAMP,
    delivery_date        TIMESTAMP,
    service_type         VARCHAR(255),
    standard_service_type VARCHAR(255),
    service_type_group   VARCHAR(20),
    billable_weight      FLOAT NOT NULL,
    actual_weight        FLOAT,
    length               FLOAT,
    width                FLOAT,
    height               FLOAT,
    cost                 DECIMAL NOT NULL,
    accessorial_service1 VARCHAR(255),
    accessorial_service2 VARCHAR(255),
    accessorial_service3 VARCHAR(255),
    accessorial_charge1  DECIMAL,
    accessorial_charge2  DECIMAL,
    accessorial_charge3  DECIMAL,
    discount             DECIMAL,
    distance             FLOAT,
    excluded             BOOLEAN
);