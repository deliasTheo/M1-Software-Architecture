create table authority_user_service (
    identity_id int not null references identity(id),
    service_id int not null references authority_service(id),
    primary key (identity_id, service_id)
);

