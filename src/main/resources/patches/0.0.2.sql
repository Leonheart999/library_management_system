create table library.books
(
    id         bigint not null
        constraint books_id_pk
            primary key,
    title     varchar
        constraint books_title_pk
            unique,
    author      varchar,
    status      varchar not null ,
    isbn        varchar
        constraint books_isbn_pk
            unique,
    user_id  bigint,
    active     boolean default true
);

alter table library.books
    owner to azry;

create unique index books_id_uindex
    on library.books (id);

create sequence library."books_id_seq";

alter sequence library."books_id_seq" owned by library.books.id;

alter table library.books
    alter column id set default nextval('library.books_id_seq'::regclass);

alter table library.books
    add constraint books_users_id_fk
        foreign key (user_id) references library.users;