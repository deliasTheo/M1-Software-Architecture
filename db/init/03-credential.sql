create table credential (
        id serial primary key,
        identity_id int not null references identity(id) on delete cascade,
        password_hash varchar(255) not null,
        created_at timestamp not null default now(),
        updated_at timestamp not null default now()
);

