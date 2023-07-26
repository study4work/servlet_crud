create table if not exists user(
    user_id integer not null auto_increment,
    name varchar(20) not null,
    primary key (user_id)
);
