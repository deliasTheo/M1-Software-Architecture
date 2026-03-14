create table authority_user_service (
    identity_id int not null references identity(id) on delete cascade,
    service_id int not null references authority_service(id) on delete cascade,
    primary key (identity_id, service_id)
);

