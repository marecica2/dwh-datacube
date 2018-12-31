CREATE SEQUENCE IF NOT EXISTS "hibernate_sequence";

CREATE SEQUENCE IF NOT EXISTS "users_seq";
CREATE TABLE IF NOT EXISTS users (
  id bigint DEFAULT nextval('users_seq') PRIMARY KEY,
  first_name varchar(50),
  last_name varchar(50)
);

CREATE SEQUENCE IF NOT EXISTS "groups_seq";
CREATE TABLE IF NOT EXISTS groups (
  id bigint DEFAULT nextval('groups_seq') PRIMARY KEY,
  name varchar(20)
);

CREATE TABLE IF NOT EXISTS "users_groups" (
  user_id bigint NOT NULL,
  group_id bigint NOT NULL,
  PRIMARY KEY (user_id, group_id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (group_id) REFERENCES groups (id)
);

CREATE TABLE IF NOT EXISTS "tenants" (
    uuid varchar(255) PRIMARY KEY,
    tenant_name VARCHAR(255),
    schema_name VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT UC_TENANT_NAME UNIQUE (tenant_name) ,
    CONSTRAINT UC_SCHEMA_NAME UNIQUE (schema_name)
);
