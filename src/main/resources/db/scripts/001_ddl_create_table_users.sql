create table if not exists users (
                        id serial primary key not null,
                        username varchar(2000),
                        created timestamp
);