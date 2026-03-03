create table identity (
    id serial primary key,
    uid varchar(255) not null unique,
    email varchar(255) not null unique,
    verified boolean not null default false,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

