INSERT INTO Genre(name) values ("genere1");
INSERT INTO Genre(name) values ("genere2");

INSERT INTO User(email, password, name, surname) values("email", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email2", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "antonio2", "ambrosio2");
INSERT INTO User(email, password, name, surname) values("email3", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "antonio2", "ambrosio2");

INSERT INTO Validator(id) values (1);
INSERT INTO Author(id) values (1);

INSERT INTO Validator(id) values (2);
INSERT INTO Author(id) values (2);

INSERT INTO Validator(id) values (3);
INSERT INTO Author(id) values (3);


SHOW STATUS LIKE 'Threads_connected';