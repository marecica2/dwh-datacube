DROP TABLE people IF EXISTS;
CREATE TABLE people
(
    person_id  BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name  VARCHAR(20)
);

DROP TABLE fact IF EXISTS;
CREATE TABLE fact
(
    id             BIGINT IDENTITY NOT NULL PRIMARY KEY,
    transaction_id VARCHAR(255),
    business_unit  VARCHAR(255)
);