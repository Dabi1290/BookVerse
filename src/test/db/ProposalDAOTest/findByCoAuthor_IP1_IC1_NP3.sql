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



INSERT INTO Proposal(status, mainAuthorId_fk) values ("pending", 5);

INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (1, 1);
INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (2, 1);
INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (3, 1);

INSERT INTO Proposal(status, mainAuthorId_fk) values ("pending", 5);

INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (1, 2);
INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (2, 2);
INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (3, 2);

INSERT INTO Proposal(status, mainAuthorId_fk) values ("pending", 5);

INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (1, 3);
INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (2, 3);
INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (3, 3);