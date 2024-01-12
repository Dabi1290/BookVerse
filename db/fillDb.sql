INSERT INTO Genre(name) values ("genere1");
INSERT INTO Genre(name) values ("genere2");

INSERT INTO User(email, password, name, surname) values("email", "password", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email2", "password", "antonio2", "ambrosio2");

INSERT INTO Validator(userId_fk) values (1);
INSERT INTO Author(userId_fk) values (1);

INSERT INTO Validator(userId_fk) values (2);
INSERT INTO Author(userId_fk) values (2);

INSERT INTO Proposal(status, mainAuthorId_fk) values ("pending", 1);

INSERT INTO Version(title, description, price, coverImage, report, ebookFile, data, proposalId_fk) values ("titolo", "descrizione", 10, "coverImagePath", "reportPath", "ebookFilePath", "2002-10-03", 1);
INSERT INTO VersionGenre(versionId_fk, genreId_fk) values (1, 1);
INSERT INTO VersionGenre(versionId_fk, genreId_fk) values (1, 2);
INSERT INTO Version(title, description, price, coverImage, report, ebookFile, data, proposalId_fk) values ("titolo2", "descrizione", 11, "coverImagePath", "reportPath", "ebookFilePath", "2003-10-03", 1);
INSERT INTO VersionGenre(versionId_fk, genreId_fk) values (2, 1);