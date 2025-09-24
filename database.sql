create database spring_contact_management_db;
use spring_contact_management_db;

create table users(
    username varchar(100) not null,
    password varchar(100) not null,
    name varchar(100) not null,
    token varchar(100),
    token_expired_at bigint,
    primary key (username),
    unique (token)
)engine = InnoDB;
select * from users;
describe users;

create table contacts(
    id varchar(100) not null,
    username varchar(100) not null,
    first_name varchar(100) not null,
    last_name varchar(100),
    phone varchar(100),
    email varchar(100),
    primary key (id),
    foreign key fk_users_contacts (username)references users (username)
)engine = InnoDB;
describe contacts;

create table addresses(
    id varchar(100) not null,
    contact_id varchar(100) not null,
    street varchar(200),
    city varchar(100),
    province varchar(100),
    country varchar(100) not null,
    postal_code varchar(20),
    primary key (id),
    foreign key fk_contacts_addresses (contact_id) references contacts (id)
)engine = InnoDB;
describe addresses;