drop database if exists webshop ;
create database if not exists webshop;
use webshop;

create table location(
id integer not null auto_increment,
`name` varchar(50) not null,
constraint pk_location_id primary key(id)
);

create table firm(
id integer not null auto_increment,
`name` varchar(50) not null,
location_id integer not null,
constraint pk_firm_id primary key(id),
constraint fk_firm_location_id foreign key(location_id) references location(id)
);

create table department(
id integer not null auto_increment,
`name` varchar(50) not null,
firm_id integer not null,
constraint ph_department_id primary key(id),
constraint fk_department_firm_id foreign key(firm_id) references firm(id)
);

create table team(
id integer not null auto_increment,
`name` varchar(50) not null,
department_id integer not null,
constraint ph_team_id primary key(id),
constraint fk_team_department_id foreign key(department_id) references department(id)
);

create table `position`(
id integer not null auto_increment,
`description` varchar(50) not null,
constraint pk_position_id primary key(id)
);

create table zipcode(
id integer not null auto_increment,
zipcode varchar(10) not null,
location_id integer not null,
constraint pk_zipcode_id primary key(id),
constraint fk_zipcode_location_id foreign key(location_id) references location(id)
);

create table address(
id integer not null auto_increment,
street varchar(100) not null,
house_number varchar(10),
zipcode_id integer not null,
constraint pk_address_id primary key(id),
constraint fk_address_zipcode_id foreign key(zipcode_id) references zipcode(id)
);

create table person (
	id integer not null auto_increment,
    firstname varchar(32) not null,
	lastname varchar(64) not null,
	date_of_birth date,
    address_id integer not null,
    constraint pk_person_id primary key (id),
    constraint fk_person_address_id foreign key (address_id) references address(id)
);

create table employee(
id integer not null auto_increment,
personnel_number mediumint not null,
position_id integer not null,
team_id integer not null,
person_id integer not null,
constraint pk_employee_id primary key(id),
constraint fk_employee_team_id foreign key(team_id) references team(id),
constraint fk_employee_position_id foreign key(position_id) references `position`(id),
constraint fk_employee_person_id foreign key(person_id) references person(id)
);


create table customer (
	id integer not null auto_increment,
    customer_no integer not null, 
    person_id integer not null,
    constraint pk_customer_id primary key (id),
    constraint fk_customer_person_id foreign key (person_id) references person(id)
);

create table warehouse (
	id integer not null auto_increment,
    address_id integer not null,
    constraint pk_warehouse_id primary key (id),
    constraint fk_warehouse_address_id foreign key (address_id) references address(id)
);

create table article (
	id integer not null auto_increment,
    `description` varchar(100) not null,
    price decimal(10,2),
    constraint pk_article_id primary key (id)
);

create table warehouse_article_rel (
	warehouse_id integer not null,
    article_id integer not null,
    article_quantity integer null,
    constraint pk_warehouse_article_rel primary key (warehouse_id, article_id),
    constraint fk_warehouse_article_rel_wareouse_id foreign key (warehouse_id) references warehouse(id),
    constraint fk_warehouse_article_rel_article_id foreign key (article_id) references article(id)
);

create table `order` (
	id integer not null auto_increment,
    customer_id integer not null,
    article_id integer not null,
    article_order_quantity integer not null references warehouse_article_rel(article_quantity),
    constraint pk_order_id primary key (id),
    constraint fk_order_customer_id foreign key (customer_id) references customer(id),
    constraint fk_order_article_id foreign key (article_id) references article(id)
);

create table order_article_rel (
	order_id integer not null auto_increment,
    article_id integer not null,
    constraint pk_order_article_rel_id primary key (order_id, article_id)
);

#Anlegen der Datensätze

insert into location(`name`) values ('essen');
insert into location(`name`) values ('berlin');
insert into location(`name`) values ('köln');
 
 insert into firm (`name`, location_id) values ('odFin', 1);
 insert into firm (`name`, location_id) values ('odITS', 1);
 
insert into zipcode(zipcode, location_id) values (123, 1);
insert into zipcode(zipcode, location_id) values (345, 2);
insert into zipcode(zipcode, location_id) values (567, 3);
 
insert into address(street, house_number, zipcode_id) values('beispiel', 1, 1);
insert into address(street, house_number, zipcode_id) values('beipsiel1', 1, 2);
insert into address(street, house_number, zipcode_id) values('beispiel2', 1, 3);

insert into department(`name`, firm_id) values ('IT', '1');

insert into team(`name`, department_id) values ('Hilfsmittel', '1');
insert into team(`name`, department_id) values ('Heilmittel', '1');
insert into team(`name`, department_id) values ('Transport/Rettungsdienst', '1');

select * from location;
SELECT * from zipcode;
select * from address;
select * from department;
select * from team;

select * from mysql.user;

select `user`,authentication_string,`plugin`,`host` from mysql.user;