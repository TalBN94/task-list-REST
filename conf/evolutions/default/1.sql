# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table chore (
  id                            uuid not null,
  owner_id                      varchar(255) not null,
  status                        integer not null,
  description                   varchar(255),
  size                          integer,
  constraint ck_chore_status check ( status in (0,1)),
  constraint ck_chore_size check ( size in (0,1,2)),
  constraint pk_chore primary key (id)
);

create table home_work (
  id                            uuid not null,
  owner_id                      varchar(255) not null,
  status                        integer not null,
  course                        varchar(255) not null,
  due_date                      timestamp not null,
  details                       varchar(255) not null,
  constraint ck_home_work_status check ( status in (0,1)),
  constraint pk_home_work primary key (id)
);

create table person (
  id                            uuid not null,
  name                          varchar(255) not null,
  email                         varchar(255) not null,
  favorite_programming_language varchar(255) not null,
  constraint uq_person_email unique (email),
  constraint pk_person primary key (id)
);


# --- !Downs

drop table if exists chore;

drop table if exists home_work;

drop table if exists person;

