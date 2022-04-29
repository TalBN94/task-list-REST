# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table person (
  id                            uuid not null,
  name                          varchar(255) not null,
  email                         varchar(255) not null,
  favorite_programming_language varchar(255) not null,
  constraint uq_person_email unique (email),
  constraint pk_person primary key (id)
);

create table task (
  id                            uuid not null,
  ownerid                       uuid not null,
  status                        integer not null,
  type                          integer not null,
  course                        varchar(255),
  due_date                      timestamp,
  details                       varchar(255),
  description                   varchar(255),
  size                          integer,
  constraint ck_task_status check ( status in (0,1)),
  constraint ck_task_type check ( type in (0,1)),
  constraint ck_task_size check ( size in (0,1,2)),
  constraint pk_task primary key (id)
);

create index ix_task_ownerid on task (ownerid);
alter table task add constraint fk_task_ownerid foreign key (ownerid) references person (id) on delete restrict on update restrict;


# --- !Downs

alter table task drop constraint if exists fk_task_ownerid;
drop index if exists ix_task_ownerid;

drop table if exists person;

drop table if exists task;

