# --- Second database schema for h2

# --- !Ups

create sequence s_bussunitid_id;

create table businessUnitId (
  id                 bigint DEFAULT nextval('s_bussunitid_id'),
  keyy               bigint(32),
  name               varchar(128)
);


# --- !Downs

drop table businessUnitId;
drop sequence s_bussunitid_id;


