create database survey charset utf8mb4;
use survey;

create table users (
  uid int primary key auto_increment,
  username varchar(20) not null unique,
  nickname varchar(20) not null,
  password char(64) not null
);

create table surveys (
  sid int primary key auto_increment,
  uid int not null,
  status varchar(10) not null comment '未发布; 已发布',
  title varchar(100) not null,
  brief varchar(200) not null,
  cover_url varchar(1024) not null,
  thanks varchar(100) not null
);

create table questions (
  qid int primary key auto_increment,
  sid int not null,
  pid int not null comment 'pid 相同的属于同一页',
  question varchar(100) not null,
  type varchar(10) not null,
  options text comment '$分割的线性结构【选项中不会出现$】'
);

create table answers (
  aid int primary key auto_increment,
  uid int not null,
  sid int not null,
  answer text not null comment '$分割的线性结构【回答中不会出现$】'
);