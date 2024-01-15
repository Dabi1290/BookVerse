INSERT INTO Genre(name) values ("genere1");
INSERT INTO Genre(name) values ("genere2");

INSERT INTO User(email, password, name, surname) values("email", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email2", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", "antonio2", "ambrosio2");

INSERT INTO Validator(id) values (1);
INSERT INTO Author(id) values (1);

INSERT INTO Validator(id) values (2);
INSERT INTO Author(id) values (2);

INSERT INTO Proposal(status, mainAuthorId_fk) values ("pending", 1);

INSERT INTO ProposalValidator(validatorId_fk, proposalId_fk) values (2, 1);

INSERT INTO Version(title, description, price, coverImage, report, ebookFile, data, proposalId_fk) values ("titolo", "descrizione", 10, "coverImagePath", "reportPath", "ebookFilePath", "2002-10-03", 1);
INSERT INTO VersionGenre(versionId_fk, genreId_fk) values (1, "genere1");
INSERT INTO VersionGenre(versionId_fk, genreId_fk) values (1, "genere2");
INSERT INTO Version(title, description, price, coverImage, report, ebookFile, data, proposalId_fk) values ("titolo2", "descrizione", 11, "coverImagePath", "reportPath", "ebookFilePath", "2003-10-03", 1);
INSERT INTO VersionGenre(versionId_fk, genreId_fk) values (2, "genere1");