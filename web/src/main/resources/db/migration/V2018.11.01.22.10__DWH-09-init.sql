CREATE SEQUENCE IF NOT EXISTS "hibernate_sequence";


CREATE SEQUENCE IF NOT EXISTS "users_seq";
CREATE TABLE IF NOT EXISTS users (
  id bigint DEFAULT nextval('users_seq') PRIMARY KEY,
  first_name varchar(255),
  last_name varchar(255)
);

