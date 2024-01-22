DROP database if exists BookVerseTest;

CREATE DATABASE BookVerseTest;
DROP USER IF EXISTS 'testClient'@'localhost';
CREATE USER 'testClient'@'localhost' IDENTIFIED BY 'testClient';
GRANT ALL ON *.* TO 'testClient'@'localhost';
FLUSH PRIVILEGES;
USE BookVerseTest;




CREATE TABLE User (
                      id int NOT NULL AUTO_INCREMENT,
                      name varchar(30) NOT NULL,
                      email varchar(30) NOT NULL,
                      surname varchar(30) NOT NULL,
                      password varchar(300) NOT NULL,

                      primary key (id)
);

CREATE TABLE Author (
                        id int NOT NULL,

                        foreign key (id) references User(id),

                        primary key (id)
);

CREATE TABLE Validator (
                           id int NOT NULL,

                           foreign key (id) references User(id),

                           primary key (id)
);

CREATE TABLE Proposal (
                          id int NOT NULL AUTO_INCREMENT,
                          status varchar(30) NOT NULL,

                          mainAuthorId_fk int NOT NULL,
                          foreign key (mainAuthorId_fk) references Author(id),

                          primary key (id)
);

CREATE TABLE Version (
                         id int NOT NULL AUTO_INCREMENT,
                         title varchar(30) NOT NULL,
                         description text NOT NULL,
                         price int NOT NULL,
                         coverImage varchar(255),
                         report varchar(255),
                         ebookFile varchar(255),
                         data Date NOT NULL,

                         proposalId_fk int NOT NULL,
                         foreign key (proposalId_fk) references Proposal (id),

                         primary key (id)
);

CREATE TABLE EBook (
                       id int NOT NULL auto_increment,
                       title varchar(30) NOT NULL,
                       price int NOT NULL,
                       description text NOT NULL,
                       inCatalog boolean Default(false),
                       ebookFile varchar(255) NOT NULL,

                       proposalId_fk int NOT NULL,
                       mainAuthorId_fk int NOT NULL,

                       foreign key (proposalId_fk) references Proposal (id),
                       foreign key (mainAuthorId_fk) references Author (id),

                       primary key (id)
);

CREATE TABLE Genre (
                       name varchar(30) NOT NULL,

                       primary key (name)
);

CREATE TABLE VersionGenre (
                              versionId_fk int NOT NULL,
                              genreId_fk varchar(30) NOT NULL,

                              foreign key (versionId_fk) references Version(id),
                              foreign key (genreId_fk) references Genre(name),

                              primary key (versionId_fk, genreId_fk)
);

CREATE TABLE EBookGenre (
                            ebookId_fk int NOT NULL,
                            genreId_fk varchar(30) NOT NULL,

                            foreign key (ebookId_fk) references EBook(id),
                            foreign key (genreId_fk) references Genre(name),

                            primary key (ebookId_fk, genreId_fk)
);

CREATE TABLE ProposalAuthor (
                                authorId_fk int NOT NULL,
                                proposalId_fk int NOT NULL,

                                foreign key (authorId_fk) references Author(id),
                                foreign key (proposalId_fk) references Proposal(id),

                                primary key(authorId_fk, proposalId_fk)
);

CREATE TABLE EBookAuthor (
                             authorId_fk int NOT NULL,
                             ebookId_fk int NOT NULL,

                             foreign key (authorId_fk) references Author(id),
                             foreign key (ebookId_fk) references EBook(id),

                             primary key(authorId_fk, ebookId_fk)
);

CREATE TABLE ProposalValidator (
                                   validatorId_fk int NOT NULL,
                                   proposalId_fk int NOT NULL,

                                   foreign key (validatorId_fk) references Validator(id),
                                   foreign key (proposalId_fk) references Proposal(id),

                                   primary key(validatorId_fk, proposalId_fk)
);