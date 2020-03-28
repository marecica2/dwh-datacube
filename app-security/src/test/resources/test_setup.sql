insert into master.roles(id, description, name) values (111, 'Admin', 'ADMIN');
insert into master.roles(id, description, name) values (222, 'User', 'USER');

insert into master.users (id, email, first_name, last_name, password, username)
values (111, 'admin@gmail.com', 'admin', 'admin', '$2a$04$EZzbSqieYfe/nFWfBWt2KeCdyq0UuDEM1ycFF8HzmlVR6sbsOnw7u',
        'admin');

insert into master.user_roles(user_id, role_id)
values (111, 111);
