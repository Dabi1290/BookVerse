DROP database if exists BookVerseTest;

CREATE DATABASE BookVerseTest;
-- CREATE USER IF NOT EXISTS 'client'@'localhost' IDENTIFIED BY 'client';
-- GRANT ALL ON bookversetest.* TO 'client'@'localhost';
USE BookVerseTest;


CREATE TABLE User (
                      id int NOT NULL AUTO_INCREMENT,
                      name varchar(30) NOT NULL,
                      email varchar(30) NOT NULL,
                      surname varchar(30) NOT NULL,
                      password varchar(300) NOT NULL,

                      primary key (id)
);

CREATE TABLE Author (
                        id int NOT NULL,

                        foreign key (id) references User(id),

                        primary key (id)
);

CREATE TABLE Validator (
                           id int NOT NULL,

                           foreign key (id) references User(id),

                           primary key (id)
);

INSERT INTO User(email, password, name, surname) values("email", "password", "antonio", "ambrosio");

INSERT INTO Validator(id) values (1);
INSERT INTO Author(id) values (1);