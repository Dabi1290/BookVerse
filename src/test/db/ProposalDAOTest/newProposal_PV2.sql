INSERT INTO User(email, password, name, surname) values("email@gmail.com", "aaaaa", "antonio", "ambrosio");
INSERT INTO User(email, password, name, surname) values("email2@gmail.com", "aaaaa", "antonio", "ambrosio");

INSERT INTO Author(id) values(1);
INSERT INTO Author(id) values(2);



INSERT INTO Proposal(status, mainAuthorId_fk) values ("pending", 1);

INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (2, 1);