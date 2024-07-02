insert into library.authorities(id, name, active)
values (0, 'USER', true);

insert into library.authorities(id, name, active)
values (1, 'ADMIN', true);

insert into library.user_authorities(id,user_id,authority_id,active) values(nextval('library.user_authorities_id_seq'),1,1,true);