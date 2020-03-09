insert into master.roles(description, name)
values ('Admin', 'ADMIN');
insert into master.roles(description, name)
values ('User', 'USER');

insert into master.users (email, first_name, last_name, password, username)
values ('admin@gmail.com', 'admin', 'admin', '$2a$04$EZzbSqieYfe/nFWfBWt2KeCdyq0UuDEM1ycFF8HzmlVR6sbsOnw7u', 'admin');

insert into master.user_roles(user_id, role_id)
values (1, 1);



-- truncate table master.user_roles restart identity cascade;
--
-- truncate table master.roles restart identity cascade;
--
-- truncate table master.users restart identity cascade;

