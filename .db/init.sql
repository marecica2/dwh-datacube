CREATE SCHEMA "common";
CREATE SCHEMA "master";
CREATE SCHEMA "000000-00000-00001";
CREATE SCHEMA "000000-00000-00002";


create table master.roles
(
    id          bigserial not null
        constraint roles_pkey primary key,
    created_on  bigint,
    description varchar(255),
    modified_on bigint,
    name        varchar(255)
);

create table master.users
(
    id         bigserial not null
        constraint users_pkey primary key,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    username   varchar(255)
);

create table master.user_roles
(
    user_id bigint not null
        constraint fk_user_roles_users references master.users,
    role_id bigint not null
        constraint fk_user_roles_roles references master.roles,
    constraint user_roles_pkey primary key (user_id, role_id)
);

insert into master.roles(description, name)
values ('Admin', 'ADMIN');
insert into master.roles(description, name)
values ('User', 'USER');

insert into master.users (email, first_name, last_name, password, username)
values ('admin@gmail.com', 'admin', 'admin', '$2a$04$EZzbSqieYfe/nFWfBWt2KeCdyq0UuDEM1ycFF8HzmlVR6sbsOnw7u', 'admin');

insert into master.user_roles(user_id, role_id)
values (1, 1);
