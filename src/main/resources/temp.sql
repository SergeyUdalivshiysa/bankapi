
drop table if exists pay;
drop table if exists acc;



create table acc
(
    id INT PRIMARY KEY AUTO_INCREMENT
);
/*

create table pay
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    send_id int not null,
    rec_id  int not null,
    foreign key (send_id) references acc (id),
    foreign key (rec_id) references acc (id)
);*/
