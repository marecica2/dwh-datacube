SET SEARCH_PATH TO "tenant0000000000000001";
DROP TABLE IF EXISTS person;
CREATE TABLE person
(
    id   int primary key not null,
    name varchar(50)
);
INSERT INTO person (id, name)
VALUES (456, 'John');

SET SEARCH_PATH TO "tenant0000000000000002";
DROP TABLE IF EXISTS person;
CREATE TABLE person
(
    id   int primary key not null,
    name varchar(50)
);
INSERT INTO person (id, name)
VALUES (789, 'Mary');
