drop table if exists Sample CASCADE 
drop sequence if exists hibernate_sequence
create sequence hibernate_sequence start with 1 increment by 1
create table Sample (id bigint not null, password varchar(255), username varchar(255), primary key (id))
drop table if exists Sample CASCADE 
drop sequence if exists hibernate_sequence
create sequence hibernate_sequence start with 1 increment by 1
create table Sample (id bigint not null, password varchar(255), username varchar(255), primary key (id))
