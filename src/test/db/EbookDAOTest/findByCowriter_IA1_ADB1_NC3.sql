INSERT INTO User(email, password, name, surname) values("email@gmail.com", "aaaaa", "antonip", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email@gmail.com", "aaaaa", "antonip", "ambrosio");

INSERT INTO Author(id) values (1);
INSERT INTO Author(id) values (2);

INSERT INTO Proposal(id, status, mainAuthorId_fk) VALUES(1, 'Completed', 1);
INSERT INTO Proposal(id, status, mainAuthorId_fk) VALUES(2, 'Completed', 1);

INSERT INTO EBook(id, title, price, description, inCatalog, ebookFile, proposalId_fk, mainAuthorId_fk) VALUES (1, 'amica geniale', 10, 'fiction rai', false, 'file.pdf', 1, 1);
INSERT INTO EBook(id, title, price, description, inCatalog, ebookFile, proposalId_fk, mainAuthorId_fk) VALUES (2, 'amica', 13, 'fiction mediaset', false, 'file2.pdf', 2, 1);

INSERT INTO EBookAuthor(authorId_fk, ebookId_fk) VALUES(2, 1);
INSERT INTO EBookAuthor(authorId_fk, ebookId_fk) VALUES(2, 2);