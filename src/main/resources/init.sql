drop table if exists user;
drop table if exists account;
drop table if exists card;
drop table if exists payment;

create table user
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar not null,
);

create table account
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    amount decimal (20, 2) default 0.00,
    user_id int not null,
    foreign key (user_id) references user (id)
);

create table card
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    number varchar not null,
    account_id int not null,
    foreign key (account_id) references account (id),
    active boolean
);

create table payment
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    amount decimal (20, 2) default 0.00,
    approved boolean,
    sender_id int not null,
    receiver_id int not null,
    foreign key (sender_id) references account (id),
    foreign key (receiver_id) references account (id)
);

insert into user (id, name) values (1, 'Victor');

insert into user (id, name) values (2, 'Vlad');

insert into account (id, amount, user_id) values (1, '200.50', 1);

insert into card (id, number, account_id, active) values (1, '1234324', 1, true);