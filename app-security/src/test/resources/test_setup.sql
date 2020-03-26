insert into roles(description, name)
values ('Admin', 'ADMIN');
insert into roles(description, name)
values ('User', 'USER');

insert into users (email, first_name, last_name, password, username)
values ('admin@gmail.com', 'admin', 'admin', '$2a$04$EZzbSqieYfe/nFWfBWt2KeCdyq0UuDEM1ycFF8HzmlVR6sbsOnw7u', 'admin');

insert into user_roles(user_id, role_id)
values (1, 1);
