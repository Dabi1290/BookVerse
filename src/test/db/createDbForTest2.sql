DROP database if exists BookVerseTest;

CREATE DATABASE BookVerseTest;
DROP USER IF EXISTS 'testClient'@'localhost';
CREATE USER 'testClient'@'localhost' IDENTIFIED BY 'testClient';
GRANT ALL ON *.* TO 'testClient'@'localhost';
FLUSH PRIVILEGES;
USE BookVerseTest;