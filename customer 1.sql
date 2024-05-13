# (D)ata (D)efinition (L)anguage
DROP DATABASE IF EXISTS webshop;
CREATE DATABASE IF NOT EXISTS webshop;
USE webshop;
CREATE TABLE location(
	id INTEGER NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
    CONSTRAINT pk_location_id PRIMARY KEY(id)
);

CREATE TABLE firm(
	id INTEGER NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
    location_id INTEGER NOT NULL,
    CONSTRAINT pk_firm_id PRIMARY KEY(id),
    CONSTRAINT fk_firm_location_id FOREIGN KEY(location_id) REFERENCES location(id)
);

CREATE TABLE department(
	id INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    firm_id INTEGER NOT NULL,
    CONSTRAINT pk_department_id PRIMARY KEY(id),
    CONSTRAINT fk_department_firm_id FOREIGN KEY(firm_id) REFERENCES firm(id)
);

CREATE TABLE team(
	id INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    department_id INTEGER NOT NULL,
    CONSTRAINT pk_team_id PRIMARY KEY(id),
    CONSTRAINT fk_team_department_id FOREIGN KEY(department_id) REFERENCES department(id)
);

CREATE TABLE `position`(
	id INTEGER NOT NULL AUTO_INCREMENT,
	`description` VARCHAR(50) NOT NULL,
    CONSTRAINT pk_position_id PRIMARY KEY(id)
);

CREATE TABLE zipcode(
	id INTEGER NOT NULL AUTO_INCREMENT,
    zipcode VARCHAR(10) NOT NULL,
    location_id INTEGER NOT NULL,
    CONSTRAINT pk_zipcode_id PRIMARY KEY(id),
    CONSTRAINT fk_zipcode_location_id FOREIGN KEY(location_id) REFERENCES location(id)
);

CREATE TABLE address(
	id INTEGER NOT NULL AUTO_INCREMENT,
    street VARCHAR(100) NOT NULL,
    house_number VARCHAR(10) NOT NULL,
    zipcode_id INTEGER NOT NULL,
    CONSTRAINT pk_address_id PRIMARY KEY(id),
    CONSTRAINT fk_address_zipcode_id FOREIGN KEY(zipcode_id) REFERENCES zipcode(id)
);

CREATE TABLE person(
	id INTEGER NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    address_id INTEGER NOT NULL,
	CONSTRAINT pk_person_id PRIMARY KEY(id),
    CONSTRAINT fk_person_address_id FOREIGN KEY(address_id) REFERENCES address(id)
);

CREATE TABLE employee(
	id INTEGER NOT NULL AUTO_INCREMENT,
    personnel_number MEDIUMINT NOT NULL,
    position_id INTEGER NOT NULL,
    team_id INTEGER NOT NULL,
    person_id INTEGER NOT NULL,
    CONSTRAINT pk_employee_id PRIMARY KEY(id),
    CONSTRAINT fk_employee_position_id FOREIGN KEY(position_id) REFERENCES `position`(id),
	CONSTRAINT fk_employee_team_id FOREIGN KEY(team_id) REFERENCES team(id),
    CONSTRAINT fk_employee_person_id FOREIGN KEY(person_id) REFERENCES person(id)
);

CREATE TABLE customer(
	id INTEGER NOT NULL AUTO_INCREMENT,
    customer_number INTEGER NOT NULL,
    person_id INTEGER NOT NULL,
	CONSTRAINT pk_customer_id PRIMARY KEY(id),
    CONSTRAINT fk_customer_person_id FOREIGN KEY(person_id) REFERENCES person(id)
);

CREATE TABLE warehouse(
	id INTEGER NOT NULL AUTO_INCREMENT,
    address_id INTEGER NOT NULL,
    CONSTRAINT pk_warehouse_id PRIMARY KEY(id),
    CONSTRAINT fk_warehouse_address_id FOREIGN KEY(address_id) REFERENCES address(id)
);

CREATE TABLE article(
	id INTEGER NOT NULL AUTO_INCREMENT,
    `description` VARCHAR(100) NOT NULL,
    price DECIMAL(10,2),
    CONSTRAINT pk_article_id PRIMARY KEY(id)
);

CREATE TABLE warehouse_article_rel(
	warehouse_id INTEGER NOT NULL,
    article_id INTEGER NOT NULL,
    article_quantity INTEGER NULL,
    CONSTRAINT pk_warehouse_article_rel PRIMARY KEY(warehouse_id, article_id),
    CONSTRAINT fk_warehouse_article_rel_warehouse_id FOREIGN KEY(warehouse_id) REFERENCES warehouse(id),
    CONSTRAINT fk_warehouse_article_rel_article_id FOREIGN KEY(article_id) REFERENCES article(id)
);

CREATE TABLE `order`(
	id INTEGER NOT NULL AUTO_INCREMENT,
    customer_id INTEGER NOT NULL,
	CONSTRAINT pk_order_id PRIMARY KEY(id),
    CONSTRAINT fk_order_customer_id FOREIGN KEY(customer_id) REFERENCES customer(id)
);

CREATE TABLE order_article_rel(
	order_id INTEGER NOT NULL,
    article_id INTEGER NOT NULL,
	article_quantity INTEGER NULL,
	CONSTRAINT pk_order_article PRIMARY KEY(order_id, article_id),
    CONSTRAINT fk_order_article_order_id FOREIGN KEY(order_id) REFERENCES `order`(id),
    CONSTRAINT fk_order_article_article_id FOREIGN KEY(article_id) REFERENCES article(id)
);

ALTER TABLE department
    ADD COLUMN head_of_department_id INTEGER NULL,
    ADD CONSTRAINT fk_department_employee_id FOREIGN KEY(head_of_department_id) REFERENCES employee(id); 

# (D)ata (Q)uery (L)anguage
# SELECT


# (D)ata (M)anipulation (L)anguage
# INSERT

# Wenn für jede Spalte ein Wert mitgegeben wird, darf auf die Angabe der Spaltennamen verzichtet werden
#INSERT INTO `location` VALUES(1, 'Essen');

# Werden nicht für jede Spalte ein Wert mitgegeben, müssen die Spalten hinter dem Tabellennamen definiert werden.
INSERT INTO `location`(`name`) VALUES('Essen'); # 1
INSERT INTO `location`(`name`) VALUES('Duisburg'); # 2

INSERT INTO firm(`name`, location_id) VALUES('opta data', 1); # 1

INSERT INTO department(`name`, firm_id) VALUES('IT-Softwareentwicklung', 1); # 1
INSERT INTO department(`name`, firm_id) VALUES('IT-Infrastruktur', 1); # 2

INSERT INTO team(`name`, department_id) VALUES('DevOps', 1); # 1
INSERT INTO team(`name`, department_id) VALUES('Datenbankadministration', 1); # 2
INSERT INTO team(`name`, department_id) VALUES('Angewandte KI', 1); # 3

INSERT INTO team(`name`, department_id) VALUES('IT-Systemadministration', 2); # 1
INSERT INTO team(`name`, department_id) VALUES('IT-Systemarchitekt', 2); # 2
INSERT INTO team(`name`, department_id) VALUES('IT-Netzwerk und Sicherheit', 2); # 3

INSERT INTO `position`(`description`) VALUES('Softwareentwickler'); # 1
INSERT INTO `position`(`description`) VALUES('Systemadministrator'); # 2
INSERT INTO `position`(`description`) VALUES('Systemarchitekt'); # 3
INSERT INTO `position`(`description`) VALUES('Sicherheitsspezialist'); # 4

INSERT INTO zipcode(zipcode, location_id) VALUES('45141', 1); # 1
INSERT INTO zipcode(zipcode, location_id) VALUES('45326', 1); # 2
INSERT INTO zipcode(zipcode, location_id) VALUES('45279', 1); # 3
INSERT INTO zipcode(zipcode, location_id) VALUES('47119', 2); # 4
INSERT INTO zipcode(zipcode, location_id) VALUES('47057', 2); # 5
INSERT INTO zipcode(zipcode, location_id) VALUES('47055', 2); # 6

INSERT INTO address(street, house_number, zipcode_id) VALUES('Berthold-Beitz-Boulevard','459', 1); # 1
INSERT INTO address(street, house_number, zipcode_id) VALUES('Krablerstr.','28', 2); # 2
INSERT INTO address(street, house_number, zipcode_id) VALUES('Briefzeile','1', 3); # 3
INSERT INTO address(street, house_number, zipcode_id) VALUES('Carpstr.','13', 4); # 4
INSERT INTO address(street, house_number, zipcode_id) VALUES('Grabenstr.','20', 5); # 5
INSERT INTO address(street, house_number, zipcode_id) VALUES('Gärtnerstr.','42', 6); # 6

# INSERT INTO person(firstname, lastname, date_of_birth, address_id) VALUES('Max', 'Mustermann', str_to_date('02.03.2023','%d.%m.%Y'), 2); # 1
INSERT INTO person(firstname, lastname, date_of_birth, address_id) VALUES('Max', 'Mustermann', '1982-03-02', 2); # 1
INSERT INTO person(firstname, lastname, date_of_birth, address_id) VALUES('Erika', 'Musterfrau', '1980-04-17', 1); # 2
INSERT INTO person(firstname, lastname, date_of_birth, address_id) VALUES('John', 'Doe', '1985-05-02', 4); # 3
INSERT INTO person(firstname, lastname, date_of_birth, address_id) VALUES('Jane', 'Doe', '1983-06-05', 3); # 4
INSERT INTO person(firstname, lastname, date_of_birth, address_id) VALUES('Bernd', 'Bizeps', '1979-04-17', 6); # 5
INSERT INTO person(firstname, lastname, date_of_birth, address_id) VALUES('Theodor', 'Trizeps', '1986-07-23', 5); # 6





SELECT * FROM person;

# UPDATE
# DELETE
