create table if not exists event (
  event_id integer not null auto_increment,
  user_id integer not null,
  file_id integer not null,
  primary key (event_id),
  foreign key (user_id) references user(user_id),
  foreign key (file_id) references file(file_id)
);