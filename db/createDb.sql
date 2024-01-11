DROP database if exists BookVerse;

CREATE DATABASE BookVerse;
CREATE USER IF NOT EXISTS 'client'@'localhost' IDENTIFIED BY 'client';
GRANT ALL ON bookverse.* TO 'client'@'localhost';
USE BookVerse;




CREATE TABLE User (
	id int NOT NULL,
    name varchar(30) NOT NULL,
    email varchar(30) NOT NULL,
    surname varchar(30) NOT NULL,
    password varchar(300) NOT NULL,
    
    primary key (id)
);

CREATE TABLE Role (
	id int NOT NULL,
    name varchar(30) NOT NULL,
    primary key(id)
);

CREATE TABLE UserRole (
	userId_fk int NOT NULL,
    roleId_fk int NOT NULL,
	
    foreign key (userId_fk) references User(id),
    foreign key (roleId_fk) references Role(id),
    
    primary key(userId_fk, roleId_fk)
);

CREATE TABLE Author (
	id int NOT NULL,
    userId_fk int NOT NULL,
    
    foreign key (userId_fk) references User(id),
    
    primary key (id)
);

CREATE TABLE Validator (
	id int NOT NULL,
    userId_fk int NOT NULL,
    
    foreign key (userId_fk) references User(id),
    
    primary key (id)
);

CREATE TABLE Proposal (
	id int NOT NULL,
    status varchar(30) NOT NULL,
    
    mainAuthorId_fk int NOT NULL,
    
    primary key (id)
); 

CREATE TABLE Version (
	id int NOT NULL,
    title varchar(30) NOT NULL,
    description text NOT NULL,
    coverImage varchar(255) NOT NULL,
    report varchar(255) NOT NULL,
    ebookFile varchar(255) NOT NULL,
    data Date NOT NULL,
    
    proposalId_fk int NOT NULL,
    foreign key (proposalId_fk) references Proposal (id),
    
    primary key (id)
);

CREATE TABLE EBook (
	id int NOT NULL,
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
	id int NOT NULL,
    name varchar(30) NOT NULL,
    
    primary key (id)
);

CREATE TABLE VersionGenre (
	versionId_fk int NOT NULL,
    genreId_fk int NOT NULL,
    
    foreign key (versionId_fk) references Version(id),
    foreign key (genreId_fk) references Genre(id),
    
    primary key (versionId_fk, genreId_fk)
);

CREATE TABLE EBookGenre (
	ebookId_fk int NOT NULL,
    genreId_fk int NOT NULL,
    
    foreign key (ebookId_fk) references EBook(id),
    foreign key (genreId_fk) references Genre(id),
    
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