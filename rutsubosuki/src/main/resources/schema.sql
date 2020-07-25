--データテーブルの作成(DB"rutsubosuki"内に作成)
create table if not exists users (
  userid int primary key AUTO_INCREMENT,
  username varchar(64) not null,
  password varchar(128) not null,
  enabled boolean not null,
  CONSTRAINT uq_username UNIQUE (username)
);

create table if not exists authorities (
  username varchar(64) not null,
  authority varchar(32) not null,
  PRIMARY KEY (username, authority)
);

create table if not exists groups (
  group_id int primary key AUTO_INCREMENT,
  grpname varchar(64) not null
);

create table if not exists group_members (
  group_id int,
  username varchar(64),
  FOREIGN KEY (group_id) REFERENCES groups (group_id),
  FOREIGN KEY (username) REFERENCES users (username),
  PRIMARY KEY (group_id, username)
);

create table if not exists articles (
  article_id int  PRIMARY KEY AUTO_INCREMENT,
  articleDate timestamp default current_timestamp,
  lastUpdate timestamp default current_timestamp on update current_timestamp,
  title text default null,
  text text default null,
  tag1 int default null,
  tag2 int default null,
  tag3 int default null,
  tag4 int default null,
  tag5 int default null,
  tag6 int default null,
  tag7 int default null,
  articleImage1 int default null,
  articleImage2 int default null,
  articleImage3 int default null,
  articleImage4 int default null
);

create table if not exists tags (
  tag_id int PRIMARY KEY AUTO_INCREMENT,
  article_id int,
  tagText text
);

create table if not exists uploads (
  image_id int PRIMARY KEY AUTO_INCREMENT,
  article_id int default null,
  image_name text,
  image_date timestamp default current_timestamp
);
