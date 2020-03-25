create table if not exists master.roles
(
    id          bigserial not null
        constraint roles_pkey primary key,
    created_on  bigint,
    description varchar(255),
    modified_on bigint,
    name        varchar(255)
);

create table if not exists master.users
(
    id         bigserial not null
        constraint users_pkey primary key,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    username   varchar(255)
);

create table if not exists master.user_roles
(
    user_id bigint not null
        constraint fk_user_roles_users references master.users,
    role_id bigint not null
        constraint fk_user_roles_roles references master.roles,
    constraint user_roles_pkey primary key (user_id, role_id)
);
