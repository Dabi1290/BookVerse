INSERT INTO User(email, password, name, surname) values("email@gmail.com", "aaaaa", "antonip", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email@gmail.com", "aaaaa", "antonip", "ambrosio");

INSERT INTO Author(id) values (1);
INSERT INTO Author(id) values (2);

INSERT INTO Proposal(id, status, mainAuthorId_fk) VALUES(1, 'Completed', 1);

INSERT INTO EBook(id, title, price, description, inCatalog, ebookFile, proposalId_fk, mainAuthorId_fk) VALUES (1, 'amica geniale', 10, 'fiction rai', false, 'file.pdf', 1, 1);
