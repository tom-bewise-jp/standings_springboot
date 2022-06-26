CREATE DATABASE IF NOT EXISTS standings;
CREATE USER IF NOT EXISTS sampleAdmin IDENTIFIED BY 'SA_pass';
CREATE USER IF NOT EXISTS sampleUser IDENTIFIED BY 'SA_user';
USE standings;
CREATE TABLE IF NOT EXISTS matches (
id INT AUTO_INCREMENT,
section INT NOT NULL,
date DATE NOT NULL,
home VARCHAR(16) NOT NULL,
away VARCHAR(16) NOT NULL,
goals_for INT NOT NULL,
goals_against INT NOT NULL,
PRIMARY KEY (id)
);
GRANT ALL ON standings.* TO 'sampleAdmin';
GRANT SELECT, INSERT, UPDATE ON standings.* TO 'sampleUser';