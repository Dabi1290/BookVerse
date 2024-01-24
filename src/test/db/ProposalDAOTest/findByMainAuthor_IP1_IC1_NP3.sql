INSERT INTO User(email, password, name, surname) values("email@gmail.com", "aaaaa", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email2@gmail.com", "aaaaa", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email3@gmail.com", "aaaaa", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email4@gmail.com", "aaaaa", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email5@gmail.com", "aaaaa", "antonio", "ambrosio");

INSERT INTO Author(id) values(1);
INSERT INTO Author(id) values(2);
INSERT INTO Author(id) values(3);
INSERT INTO Author(id) values(4);
INSERT INTO Author(id) values(5);



INSERT INTO Proposal(status, mainAuthorId_fk) values ("pending", 1);
INSERT INTO Proposal(status, mainAuthorId_fk) values ("pending", 1);

INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (2, 1);
