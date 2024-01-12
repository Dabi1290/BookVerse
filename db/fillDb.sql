INSERT INTO User(email, password, name, surname) values("email", "password", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email2", "password", "antonio2", "ambrosio2");

INSERT INTO Validator(userId_fk) values (1);
INSERT INTO Author(userId_fk) values (1);

INSERT INTO Validator(userId_fk) values (2);
INSERT INTO Author(userId_fk) values (2);