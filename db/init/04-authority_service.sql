create table authority_service (
    id serial primary key,
    code varchar(255) not null,
    label varchar(255),
    created_at timestamp not null default now()
);

insert into authority_service (code, label) values ('A', 'Service A');
