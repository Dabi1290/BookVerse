INSERT INTO Genre(name) values ("Horror");
INSERT INTO Genre(name) values ("Fantasy");
INSERT INTO Genre(name) values ("Action");
INSERT INTO Genre(name) values ("Kids");
INSERT INTO Genre(name) values ("Cook");
INSERT INTO Genre(name) values ("Adventure");
INSERT INTO Genre(name) values ("Politics");
INSERT INTO Genre(name) values ("Languages");
INSERT INTO Genre(name) values ("Crime");
INSERT INTO Genre(name) values ("Thriller");
INSERT INTO Genre(name) values ("Sci-fi");
INSERT INTO Genre(name) values ("Society");
INSERT INTO Genre(name) values ("Law");
INSERT INTO Genre(name) values ("Biographies");
INSERT INTO Genre(name) values ("History");
INSERT INTO Genre(name) values ("Archaeology");

INSERT INTO User(email, password, name, surname) values("email1@gmail.com", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email2@gmail.com", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "davide", "amoruso");
INSERT INTO User(email, password, name, surname) values("email3@gmail.com", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "samuele", "russo");
INSERT INTO User(email, password, name, surname) values("email4@gmail.com", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "alessandro", "coppola");
INSERT INTO User(email, password, name, surname) values("email@email.com", "10123a8011fbd753ae044de647e70d6c7c3491c0b686d5e88d721158aeee5ca3e74a609b0e5e428a6ea53d059fea2e8df3d08f96c977c93703b06d90d0cc9d29", "paperon", "de paperoni");
INSERT INTO Validator(id) values (1);
INSERT INTO Author(id) values (1);

INSERT INTO Validator(id) values (2);
INSERT INTO Author(id) values (2);

INSERT INTO Validator(id) values (3);
INSERT INTO Author(id) values (3);


INSERT INTO Author(id) values (5);
INSERT INTO Validator(id) values (4);


SHOW STATUS LIKE 'Threads_connected';