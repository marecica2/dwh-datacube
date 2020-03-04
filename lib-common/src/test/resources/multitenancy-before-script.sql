DROP TABLE IF EXISTS "000000-00000-00001".person;
DROP TABLE IF EXISTS "000000-00000-00002".person;

CREATE TABLE "000000-00000-00001".person
(
    id   int primary key not null,
    name varchar(50)
);
CREATE TABLE "000000-00000-00002".person
(
    id   int primary key not null,
    name varchar(50)
);

INSERT INTO "000000-00000-00001".person (id, name)
VALUES (456, 'John');
INSERT INTO "000000-00000-00002".person (id, name)
VALUES (789, 'Mary');
