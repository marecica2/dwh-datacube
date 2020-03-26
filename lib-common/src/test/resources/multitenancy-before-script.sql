truncate  master.tenants cascade;
insert into master.tenants (id, name, description, created_on, modified_on)
values ('tenant0000000000000001', 'tenant-1', 'Tenant BMW', null, null);
insert into master.tenants (id, name, description, created_on, modified_on)
values ('tenant0000000000000002', 'tenant-2', 'Tenant Audi', null, null);

SET SEARCH_PATH TO "tenant0000000000000001";
DROP TABLE IF EXISTS "tenant0000000000000001".person;
CREATE TABLE "tenant0000000000000001".person
(
    id   int primary key not null,
    name varchar(50)
);
INSERT INTO "tenant0000000000000001".person (id, name)
VALUES (456, 'John');

SET SEARCH_PATH TO "tenant0000000000000002";
DROP TABLE IF EXISTS "tenant0000000000000002".person;
CREATE TABLE "tenant0000000000000002".person
(
    id   int primary key not null,
    name varchar(50)
);
INSERT INTO "tenant0000000000000002".person (id, name)
VALUES (789, 'Mary');
