##################################
# (D)ata (D)efinition (L)anguage #
##################################

DROP DATABASE IF EXISTS webshop;
CREATE DATABASE IF NOT EXISTS webshop;
USE webshop;
SET SQL_SAFE_UPDATES = 1;

CREATE TABLE location
(
    id     INTEGER     NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    CONSTRAINT pk_location_id PRIMARY KEY (id)
);

CREATE TABLE firm
(
    id          INTEGER     NOT NULL AUTO_INCREMENT,
    `name`      VARCHAR(50) NOT NULL,
    location_id INTEGER     NOT NULL,
    CONSTRAINT pk_firm_id PRIMARY KEY (id),
    CONSTRAINT fk_firm_location_id FOREIGN KEY (location_id) REFERENCES location (id)
);

CREATE TABLE department
(
    id      INTEGER     NOT NULL AUTO_INCREMENT,
    `name`  VARCHAR(50) NOT NULL,
    firm_id INTEGER     NOT NULL,
    CONSTRAINT pk_department_id PRIMARY KEY (id),
    CONSTRAINT fk_department_firm_id FOREIGN KEY (firm_id) REFERENCES firm (id)
);

CREATE TABLE team
(
    id            INTEGER     NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(50) NOT NULL,
    department_id INTEGER     NOT NULL,
    CONSTRAINT pk_team_id PRIMARY KEY (id),
    CONSTRAINT fk_team_department_id FOREIGN KEY (department_id) REFERENCES department (id)
);

CREATE TABLE `position`
(
    id            INTEGER     NOT NULL AUTO_INCREMENT,
    `description` VARCHAR(50) NOT NULL,
    CONSTRAINT pk_position_id PRIMARY KEY (id)
);

CREATE TABLE zipcode
(
    id          INTEGER     NOT NULL AUTO_INCREMENT,
    zipcode     VARCHAR(10) NOT NULL,
    location_id INTEGER     NOT NULL,
    CONSTRAINT pk_zipcode_id PRIMARY KEY (id),
    CONSTRAINT fk_zipcode_location_id FOREIGN KEY (location_id) REFERENCES location (id)
);

CREATE TABLE address
(
    id           INTEGER      NOT NULL AUTO_INCREMENT,
    street       VARCHAR(100) NOT NULL,
    house_number VARCHAR(10)  NOT NULL,
    zipcode_id   INTEGER      NOT NULL,
    CONSTRAINT pk_address_id PRIMARY KEY (id),
    CONSTRAINT fk_address_zipcode_id FOREIGN KEY (zipcode_id) REFERENCES zipcode (id)
);

CREATE TABLE person
(
    id            INTEGER     NOT NULL AUTO_INCREMENT,
    firstname     VARCHAR(30) NOT NULL,
    lastname      VARCHAR(50) NOT NULL,
    date_of_birth DATE        NOT NULL,
    address_id    INTEGER     NOT NULL,
    CONSTRAINT pk_person_id PRIMARY KEY (id),
    CONSTRAINT fk_person_address_id FOREIGN KEY (address_id) REFERENCES address (id)
);

CREATE TABLE employee
(
    person_id        INTEGER   NOT NULL,
    personnel_number MEDIUMINT NOT NULL,
    position_id      INTEGER   NOT NULL,
    team_id          INTEGER   NOT NULL,
    CONSTRAINT pk_employee_id PRIMARY KEY (person_id),
    CONSTRAINT fk_employee_position_id FOREIGN KEY (position_id) REFERENCES `position` (id),
    CONSTRAINT fk_employee_team_id FOREIGN KEY (team_id) REFERENCES team (id),
    CONSTRAINT fk_employee_person_id FOREIGN KEY (person_id) REFERENCES person (id)
);

CREATE TABLE customer
(
    person_id       INTEGER NOT NULL,
    customer_number INTEGER NOT NULL,
    CONSTRAINT pk_customer_id PRIMARY KEY (person_id),
    CONSTRAINT fk_customer_person_id FOREIGN KEY (person_id) REFERENCES person (id)
);

CREATE TABLE warehouse
(
    id         INTEGER NOT NULL AUTO_INCREMENT,
    address_id INTEGER NOT NULL,
    CONSTRAINT pk_warehouse_id PRIMARY KEY (id),
    CONSTRAINT fk_warehouse_address_id FOREIGN KEY (address_id) REFERENCES address (id)
);

CREATE TABLE article
(
    id            INTEGER      NOT NULL AUTO_INCREMENT,
    `description` VARCHAR(100) NOT NULL,
    price         DECIMAL(10, 2),
    CONSTRAINT pk_article_id PRIMARY KEY (id)
);

CREATE TABLE warehouse_article_rel
(
    warehouse_id     INTEGER NOT NULL,
    article_id       INTEGER NOT NULL,
    article_quantity INTEGER NULL,
    CONSTRAINT pk_warehouse_article_rel PRIMARY KEY (warehouse_id, article_id),
    CONSTRAINT fk_warehouse_article_rel_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouse (id),
    CONSTRAINT fk_warehouse_article_rel_article_id FOREIGN KEY (article_id) REFERENCES article (id)
);

CREATE TABLE `order`
(
    id        INTEGER NOT NULL AUTO_INCREMENT,
    person_id INTEGER NOT NULL,
    CONSTRAINT pk_order_id PRIMARY KEY (id),
    CONSTRAINT fk_order_customer_id FOREIGN KEY (person_id) REFERENCES customer (person_id)
);

CREATE TABLE order_article_rel
(
    order_id         INTEGER NOT NULL,
    article_id       INTEGER NOT NULL,
    article_quantity INTEGER NULL,
    CONSTRAINT pk_order_article PRIMARY KEY (order_id, article_id),
    CONSTRAINT fk_order_article_order_id FOREIGN KEY (order_id) REFERENCES `order` (id),
    CONSTRAINT fk_order_article_article_id FOREIGN KEY (article_id) REFERENCES article (id)
);

ALTER TABLE department
    ADD COLUMN head_of_department_id INTEGER NULL,
    ADD CONSTRAINT fk_department_employee_id FOREIGN KEY (head_of_department_id) REFERENCES employee (person_id)
        ON DELETE SET NULL;

DROP TRIGGER IF EXISTS delete_person_after_employee_deletion;

DELIMITER //

CREATE TRIGGER delete_person_after_employee_deletion
    AFTER DELETE
    ON employee
    FOR EACH ROW
BEGIN
    DELETE FROM person WHERE person.id = OLD.person_id;
END;
//

DELIMITER ;

# (D)ata (Q)uery (L)anguage
# SELECT

####################################
# (D)ata (M)anipulation (L)anguage #
####################################

### INSERT ###

# Location
# Wenn für jede Spalte ein Wert mitgegeben wird, darf auf die Angabe der Spaltennamen verzichtet werden
#INSERT INTO `location` VALUES(1, 'Essen');

# Werden nicht für jede Spalte ein Wert mitgegeben, müssen die Spalten hinter dem Tabellennamen definiert werden.
INSERT INTO `location`(`name`)
VALUES ('Essen'); # 1
INSERT INTO `location`(`name`)
VALUES ('Duisburg');
# 2

# Firm
INSERT INTO firm(`name`, location_id)
VALUES ('opta data', 1);
# 1

# Department
INSERT INTO department(`name`, firm_id)
VALUES ('IT-Softwareentwicklung', 1); # 1
INSERT INTO department(`name`, firm_id)
VALUES ('IT-Infrastruktur', 1);
# 2

# Team
INSERT INTO team(`name`, department_id)
VALUES ('DevOps', 1); # 1
INSERT INTO team(`name`, department_id)
VALUES ('Datenbankadministration', 1); # 2
INSERT INTO team(`name`, department_id)
VALUES ('Angewandte KI', 1); # 3

INSERT INTO team(`name`, department_id)
VALUES ('IT-Systemadministration', 2); # 4
INSERT INTO team(`name`, department_id)
VALUES ('IT-Systemarchitekt', 2); # 5
INSERT INTO team(`name`, department_id)
VALUES ('IT-Netzwerk und Sicherheit', 2);
# 6

# Position
INSERT INTO `position`(`description`)
VALUES ('Softwareentwickler'); # 1
INSERT INTO `position`(`description`)
VALUES ('Systemadministrator'); # 2
INSERT INTO `position`(`description`)
VALUES ('Systemarchitekt'); # 3
INSERT INTO `position`(`description`)
VALUES ('Sicherheitsspezialist');
# 4

# Zipcode
INSERT INTO zipcode(zipcode, location_id)
VALUES ('45141', 1); # 1
INSERT INTO zipcode(zipcode, location_id)
VALUES ('45326', 1); # 2
INSERT INTO zipcode(zipcode, location_id)
VALUES ('45279', 1); # 3
INSERT INTO zipcode(zipcode, location_id)
VALUES ('47119', 2); # 4
INSERT INTO zipcode(zipcode, location_id)
VALUES ('47057', 2); # 5
INSERT INTO zipcode(zipcode, location_id)
VALUES ('47055', 2);
# 6

# Address
INSERT INTO address(street, house_number, zipcode_id)
VALUES ('Berthold-Beitz-Boulevard', '459', 1); # 1
INSERT INTO address(street, house_number, zipcode_id)
VALUES ('Krablerstr.', '28', 2); # 2
INSERT INTO address(street, house_number, zipcode_id)
VALUES ('Briefzeile', '1', 3); # 3
INSERT INTO address(street, house_number, zipcode_id)
VALUES ('Carpstr.', '13', 4); # 4
INSERT INTO address(street, house_number, zipcode_id)
VALUES ('Grabenstr.', '20', 5); # 5
INSERT INTO address(street, house_number, zipcode_id)
VALUES ('Gärtnerstr.', '42', 6);
# 6

# Person
# INSERT INTO person(firstname, lastname, date_of_birth, address_id) VALUES('Max', 'Mustermann', str_to_date('02.03.2023','%d.%m.%Y'), 2); # 1
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Max', 'Mustermann', '1982-03-02', 2); # 1
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Erika', 'Musterfrau', '1980-04-17', 1); # 2
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('John', 'Doe', '1985-05-02', 4); # 3
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Jane', 'Doe', '1983-06-05', 3); # 4
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Bernd', 'Bizeps', '1979-04-17', 6); # 5
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Theodor', 'Trizeps', '1986-07-23', 6); # 6

INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Max', 'Mastermann', '1982-03-02', 2); # 7
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Claudia', 'Musterfrau', '1980-04-17', 1); # 8
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Bernhard', 'Doe', '1985-05-02', 4); # 9
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Kevin', 'Bizeps', '1983-06-05', 3); # 10
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Bernd', 'Bizeps', '1979-04-17', 6); # 11
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Günther', 'Jauch', '1986-07-23', 6); # 12

INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Günther', 'Oberholz', '1982-03-02', 2); # 13
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Minnie', 'Mouse', '1980-04-17', 1); # 14
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Gertrud', 'Doe', '1985-05-02', 4); # 15
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Jim', 'Browning', '1983-06-05', 3); # 16
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Uwe', 'Herklotz', '1979-04-17', 6); # 17
INSERT INTO person(firstname, lastname, date_of_birth, address_id)
VALUES ('Johnny', 'Depp', '1986-07-23', 6);
# 18

# Employee
INSERT INTO employee
VALUES (1, 1234, 1, 1);
INSERT INTO employee
VALUES (2, 1235, 1, 3);
INSERT INTO employee
VALUES (3, 1236, 2, 4);
INSERT INTO employee
VALUES (4, 1237, 4, 4);

# Customer
INSERT INTO customer
VALUES (5, 12345);
INSERT INTO customer
VALUES (6, 23456);

# Warehouse
INSERT INTO warehouse(address_id)
VALUES (1);

# Article
INSERT INTO article(`description`, price)
VALUES ('Bananen', 1.99); # 1
INSERT INTO article(`description`, price)
VALUES ('Gurken', 0.89); # 2
INSERT INTO article(`description`, price)
VALUES ('Tomaten', 2.29); # 3
INSERT INTO article(`description`, price)
VALUES ('Haferflocken', 0.79); # 4
INSERT INTO article(`description`, price)
VALUES ('Cornfalkes', 2.89); # 5
INSERT INTO article(`description`, price)
VALUES ('Nudeln', 0.79); # 6
INSERT INTO article(`description`, price)
VALUES ('Magarine', 0.99); # 7
INSERT INTO article(`description`, price)
VALUES ('Drachenfrucht', 3.59);
# 8

# Warehouse Article relation
INSERT INTO warehouse_article_rel(warehouse_id, article_id, article_quantity)
VALUES (1, 1, 30);
INSERT INTO warehouse_article_rel(warehouse_id, article_id, article_quantity)
VALUES (1, 2, 20);
INSERT INTO warehouse_article_rel(warehouse_id, article_id, article_quantity)
VALUES (1, 3, 50);
INSERT INTO warehouse_article_rel(warehouse_id, article_id, article_quantity)
VALUES (1, 4, 100);
INSERT INTO warehouse_article_rel(warehouse_id, article_id, article_quantity)
VALUES (1, 5, 30);
INSERT INTO warehouse_article_rel(warehouse_id, article_id, article_quantity)
VALUES (1, 6, 100);
INSERT INTO warehouse_article_rel(warehouse_id, article_id, article_quantity)
VALUES (1, 7, 200);
INSERT INTO warehouse_article_rel(warehouse_id, article_id, article_quantity)
VALUES (1, 8, 20);

# UPDATE
UPDATE department
SET head_of_department_id = 1
WHERE id = 1;
UPDATE person
SET firstname     = 'Mickey',
    lastname      = 'Mouse',
    date_of_birth = '1928-05-15'
WHERE id = 5;
UPDATE article
SET price = 3.49
WHERE id = 8;

# DELETE
#DELETE FROM customer WHERE person_id = 5;
#DELETE FROM employee WHERE person_id = 1;

#############################
# (D)ata (Q)uery (L)anguage #
#############################

# SELECT
/*
SELECT * FROM person;
SELECT firstname, lastname FROM person; 
SELECT firstname, lastname FROM person WHERE firstname <> 'Erika';
SELECT firstname, lastname FROM person WHERE firstname LIKE 'J%';
SELECT firstname, lastname FROM person WHERE lastname LIKE '%e';
SELECT firstname, lastname FROM person WHERE lastname LIKE 'D%e';
SELECT firstname, lastname FROM person WHERE firstname LIKE '%i%';
SELECT id, firstname, lastname FROM person WHERE id > 2;
SELECT id, firstname, lastname FROM person WHERE id >= 2;
SELECT * FROM person WHERE id IN(2, 4, 6);
SELECT * FROM person WHERE id BETWEEN 3 AND 5;

SELECT * FROM person WHERE address_id IN(SELECT id FROM address WHERE street LIKE 'Gärtner%');

SELECT * FROM person ORDER BY lastname ASC;
SELECT * FROM person ORDER BY lastname DESC;
SELECT * FROM person ORDER BY 4 DESC;

SELECT firstname, lastname FROM person WHERE firstname LIKE '%i%' ORDER BY lastname DESC;
SELECT firstname AS 'Vorname', lastname AS 'Nachname' FROM person a WHERE a.firstname LIKE '%i%' ORDER BY a.lastname DESC;


SELECT COUNT(id) FROM person;

# Aufgaben dazu erstellen.
SELECT lastname, count(lastname) AS 'Anzahl' FROM person WHERE id < 13 GROUP BY lastname HAVING Anzahl > 1 ORDER BY Anzahl DESC LIMIT 2;

*/

select *
from department
         left join employee on employee.person_id = department.head_of_department_id;

CREATE VIEW person_data AS
SELECT person.id,
       firstname,
       lastname,
       date_of_birth,
       street,
       house_number,
       zipcode,
       `name`
FROM person
         JOIN address ON person.address_id = address.id
         JOIN zipcode ON address.zipcode_id = zipcode.id
         JOIN location ON zipcode.location_id = location.id;

# SELECT * FROM person_data;

# INSERT INTO person(firstname, lastname, date_of_birth, address_id) VALUES('John', 'Doe', '1985-05-02', 4); # 3


# TRIGGER Funktionen für die Bestellung (order und order_article_rel)

CREATE USER 'halil'@'localhost' IDENTIFIED BY 'test';

REVOKE SELECT ON *.* FROM 'halil'@'localhost';

CREATE TABLE warehouse
(
    id         INTEGER NOT NULL,
    address_id INTEGER NOT NULL,
    CONSTRAINT pk_warehouse_id PRIMARY KEY (id),
);
CREATE TABLE article
(
    id            INTEGER      NOT NULL,
    `description` VARCHAR(100) NOT NULL,
    price         DECIMAL(10, 2),
    CONSTRAINT pk_article_id PRIMARY KEY (id)
);
CREATE TABLE warehouse_article_rel
(
    warehouse_id     INTEGER NOT NULL,
    article_id       INTEGER NOT NULL,
    article_quantity INTEGER NULL,
    CONSTRAINT pk_warehouse_article_rel PRIMARY KEY (warehouse_id, article_id),
    CONSTRAINT fk_warehouse_article_rel_warehouse_id FOREIGN KEY (warehouse _id) REFERENCES warehouse (id),
    CONSTRAINT fk_warehouse_article_rel_article_id FOREIGN KEY (article_id) REFE
        RENCES article(id)
);
ALTER
TRIGGER "quantity_update" instead of
insert on
    "dba"."warehouse_article_rel"
    referencing new as "NEU"
    for each row
begin



CREATE TABLE warehouse
(
    id         INTEGER NOT NULL,
    address_id INTEGER NOT NULL,
    CONSTRAINT pk_warehouse_id PRIMARY KEY (id)
);

CREATE TABLE article
(
    id          INTEGER      NOT NULL,
    description VARCHAR(100) NOT NULL,
    price       DECIMAL(10, 2),
    CONSTRAINT pk_article_id PRIMARY KEY (id)
);

CREATE TABLE warehouse_article_rel
(
    warehouse_id     INTEGER NOT NULL,
    article_id       INTEGER NOT NULL,
    article_quantity INTEGER NULL,
    CONSTRAINT pk_warehouse_article_rel PRIMARY KEY (warehouse_id, article_id),
    CONSTRAINT fk_warehouse_article_rel_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouse (id),
    CONSTRAINT fk_warehouse_article_rel_article_id FOREIGN KEY (article_id) REFERENCES article (id)
);

DELIMITER //

CREATE TRIGGER quantity_update
    BEFORE INSERT
    ON warehouse_article_rel
    FOR EACH ROW
BEGIN
    -- Beispielaktion: Stellen Sie sicher, dass die Menge nicht negativ ist.
    IF NEW.article_quantity < 0 THEN
        SET NEW.article_quantity = 0;
    END IF;

    -- Weitere Aktionen können hier hinzugefügt werden.
END;
//

DELIMITER ;