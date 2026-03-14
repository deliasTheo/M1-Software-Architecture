create table identity (
    id serial primary key,
    uid varchar(255) not null unique,
    email varchar(255) not null unique,
    role_id int not null references role(id),
    verified boolean not null default false,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

