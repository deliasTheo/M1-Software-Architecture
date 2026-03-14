create table validation_token (
    id serial primary key,
    identity_id int not null references identity(id) on delete cascade,
    used boolean not null default false,
    token_hash varchar(255) not null,
    created_at timestamp not null default now(),
    expires_at timestamp not null
);