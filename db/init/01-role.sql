create table role (
    id serial primary key,
    name varchar(255) not null DEFAULT 'user',
    description varchar(255) DEFAULT 'User role'
);

INSERT INTO role (name, description) VALUES ('user', 'User role');
INSERT INTO role (name, description) VALUES ('admin', 'Admin role');
INSERT INTO role (name, description) VALUES ('modo', 'Moderator role');