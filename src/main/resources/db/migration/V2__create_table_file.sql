create table if not exists file(
    file_id integer not null auto_increment,
    name varchar(20) not null,
    file_path varchar(100),
    primary key (file_id)
);