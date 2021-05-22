drop table if exists user cascade;
drop table if exists account cascade;
drop table if exists card cascade;
drop table if exists payment cascade;
drop table if exists counterparty cascade;

create table user
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    name         varchar not null,
    legal_entity boolean
);

create table account
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    number  bigint auto_increment (1000000000000000, 1) not null,
    amount  decimal(20, 2) default 0.00,
    user_id int                                         not null,
    foreign key (user_id) references user (id)
);

create table card
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    number     bigint auto_increment (1000000000000000, 1) not null,
    account_id int                                         not null,
    foreign key (account_id) references account (id),
    active     boolean
);

create table payment
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    amount      decimal(20, 2) default 0.00,
    approved    boolean        default false,
    sender_id   int not null,
    receiver_id int not null,
    foreign key (sender_id) references account (id),
    foreign key (receiver_id) references account (id)
);

create table counterparty
(
    party_id        int not null,
    counterparty_id int not null,
    foreign key (party_id) references user (id),
    foreign key (counterparty_id) references user (id),
    unique (party_id, counterparty_id)
);

insert into user (id, name, legal_entity)
values (1, 'Victor', false);

insert into user (id, name, legal_entity)
values (2, 'Vlad', false);

insert into user (id, name, legal_entity)
values (3, 'Kolya', false);

insert into account (id, amount, user_id)
values (1, '200.50', 1);

insert into account (id, amount, user_id)
values (2, '2', 2);

insert into card (id, account_id, active)
values (1, 1, true);

insert into card (id, account_id, active)
values (2, 1, false);

insert into counterparty (party_id, counterparty_id)
values (1, 2);

insert into payment (amount, approved, sender_id, receiver_id)
values (1, false, 2, 1);

insert into payment (amount, approved, sender_id, receiver_id)
values (100, true, 2, 1)