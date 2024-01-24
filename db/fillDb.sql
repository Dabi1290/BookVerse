INSERT INTO Genre(name) values ("genere1");
INSERT INTO Genre(name) values ("genere2");

INSERT INTO User(email, password, name, surname) values("email", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email2", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "antonio2", "ambrosio2");
INSERT INTO User(email, password, name, surname) values("email3", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "antonio2", "ambrosio2");
INSERT INTO User(email, password, name, surname) values("email@email.com", "10123a8011fbd753ae044de647e70d6c7c3491c0b686d5e88d721158aeee5ca3e74a609b0e5e428a6ea53d059fea2e8df3d08f96c977c93703b06d90d0cc9d29", "antonio2", "ambrosio2");
INSERT INTO Validator(id) values (1);
INSERT INTO Author(id) values (1);

INSERT INTO Validator(id) values (2);
INSERT INTO Author(id) values (2);

INSERT INTO Validator(id) values (3);
INSERT INTO Author(id) values (3);


INSERT INTO Author(id) values (4);


SHOW STATUS LIKE 'Threads_connected';