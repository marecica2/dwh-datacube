CREATE SCHEMA "master";
CREATE SCHEMA "tenant0000000000000001";
CREATE SCHEMA "tenant0000000000000002";

create table master.roles
(
    id          bigserial not null constraint roles_pkey primary key,
    description varchar(255),
    name        varchar(255),
    created_on  timestamp,
    modified_on timestamp
);

create table master.users
(
    id         bigserial not null constraint users_pkey primary key,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    username   varchar(255),
    created_on  timestamp,
    modified_on timestamp
);

create table master.user_roles
(
    user_id bigint not null constraint fk_user_roles_users references master.users,
    role_id bigint not null constraint fk_user_roles_roles references master.roles,
    constraint user_roles_pkey primary key (user_id, role_id)
);

create table if not exists master.tenants
(
    id          varchar(30) not null constraint tenants_pkey primary key,
    name        varchar(255),
    description varchar(255),
    created_on  timestamp,
    modified_on timestamp
);
create sequence if not exists master.tenants_id_seq;

create table if not exists master.user_tenants
(
    user_id   bigint      not null constraint fk_user_roles_users references master.users,
    tenant_id varchar(30) not null constraint fk_user_tenants references master.tenants
);

insert into master.tenants (id, name, description, created_on, modified_on)
values ('tenant' || LPAD(nextval('master.tenants_id_seq')::text, 16, '0'), 'tenant-1', 'DevTenant 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into master.tenants (id, name, description, created_on, modified_on)
values ('tenant' || LPAD(nextval('master.tenants_id_seq')::text, 16, '0'), 'tenant-2', 'DevTenant 2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into master.roles(description, name, created_on, modified_on)
values ('Admin', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into master.roles(description, name, created_on, modified_on)
values ('User', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- setup admin user
insert into master.users (email, first_name, last_name, password, username, created_on, modified_on)
values ('admin@gmail.com', 'Super', 'Admin', '$2a$04$EZzbSqieYfe/nFWfBWt2KeCdyq0UuDEM1ycFF8HzmlVR6sbsOnw7u', 'admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into master.user_roles(user_id, role_id)
values (1, 1);
insert into master.user_tenants (user_id, tenant_id)
values (1, 'tenant0000000000000001');

-- setup tenant user
insert into master.users (email, first_name, last_name, password, username, created_on, modified_on)
values ('user@gmail.com', 'John', 'Doe', '$2a$04$EZzbSqieYfe/nFWfBWt2KeCdyq0UuDEM1ycFF8HzmlVR6sbsOnw7u', 'user', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into master.user_roles(user_id, role_id)
values (2, 2);
insert into master.user_tenants (user_id, tenant_id)
values (2, 'tenant0000000000000002');
