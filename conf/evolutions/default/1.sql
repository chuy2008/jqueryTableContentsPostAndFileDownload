# --- First database schema for h2

# --- !Ups

create sequence s_bussunitstats_id;

create table businessUnitStats (
  id                 bigint DEFAULT nextval('s_bussunitstats_id'),
  busUnitKey         bigint(32),
  quarter            bigint(8),
  quarterSales       bigint(64),
  projectsClosed     bigint(32),
  newProjects        bigint(32),
  manHoursSalesTasks bigint(32)
);


# --- !Downs

drop table businessUnitStats;
drop sequence s_bussunitstats_id;

