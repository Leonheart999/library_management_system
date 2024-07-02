create schema library;

----------------------USERS
create table library.users
(
    id         bigint not null
        constraint users_id_pk
            primary key,
    user_name     varchar
        constraint users_user_name_pk
            unique,
    password  varchar,
    active     boolean default true
);

alter table library.users
    owner to azry;

create unique index users_id_uindex
    on library.users (id);

create sequence library."users_id_seq";

alter sequence library."users_id_seq" owned by library.users.id;

alter table library.users
    alter column id set default nextval('library.users_id_seq'::regclass);


----------------------AUTHORITIES

create table library.authorities
(
    id        bigint               not null
        constraint authorities_pk
            primary key,
    name      varchar              not null,
    active    boolean default TRUE not null
);

alter table library.authorities
    owner to azry;


create unique index if not exists authorities_id_uindex
    on library.authorities (id);


create table library.user_authorities
(
    id           bigint
        constraint user_authorities_pk
            primary key,
    user_id      bigint               not null
        constraint user_authorities_users_id_fk
            references library.users,
    authority_id bigint               not null
        constraint user_authorities_authorities_id_fk
            references library.authorities,
    active       boolean default true not null
);

alter table library.user_authorities
    owner to azry;


create sequence library."user_authorities_id_seq";

alter sequence library."user_authorities_id_seq" owned by library.user_authorities.id;

alter table library.user_authorities
    alter column id set default nextval('library.user_authorities_id_seq'::regclass);

----------------------add default user
insert into library.users(id,user_name, password) values (nextval('library.users_id_seq'),'admin','$2a$10$Lv4UJMeipj4AyAAibqSrv.iwx0s1FD.4Ar6Wh5ywHxOlrwORi.ADq');
