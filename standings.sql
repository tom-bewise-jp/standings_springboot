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
GRANT SELECT, INSERT, UPDATE, DELETE ON standings.* TO 'sampleUser';
CREATE OR REPLACE VIEW standings AS
SELECT team, sum(point) AS pts, sum(win) AS W, sum(draw) AS D, sum(lose) AS L,
sum(GF) AS GFs, sum(GA) AS GAs, sum(goals_difference) AS GDs
FROM (
SELECT home AS team, 
IF (goals_for > goals_against, 3, IF (goals_for = goals_against, 1, 0)) AS point,
IF (goals_for > goals_against, 1, 0) AS win,
IF (goals_for = goals_against, 1, 0) AS draw,
IF (goals_for < goals_against, 1, 0) AS lose,
goals_for AS GF, goals_against AS GA, goals_for - goals_against AS goals_difference FROM matches
UNION ALL
SELECT away AS team, 
IF (goals_for < goals_against, 3, IF (goals_for = goals_against, 1, 0)) AS point,
IF (goals_for < goals_against, 1, 0) AS win,
IF (goals_for = goals_against, 1, 0) AS draw,
IF (goals_for > goals_against, 1, 0) AS lose,
goals_against AS GA, goals_for AS GF, goals_against - goals_for AS goals_difference FROM matches) AS tbl
GROUP BY team ORDER BY pts DESC, GDs DESC, GFs DESC;

CREATE TABLE IF NOT EXISTS teams (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL UNIQUE,
abbr VARCHAR(16) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS matches2 (
id INT AUTO_INCREMENT,
section INT NOT NULL,
date DATE NOT NULL,
home_id INT NOT NULL,
away_id INT NOT NULL,
goals_for INT NOT NULL,
goals_against INT NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (home_id)
	REFERENCES teams(id)
	ON DELETE RESTRICT,
FOREIGN KEY (away_id)
	REFERENCES teams(id)
	ON DELETE RESTRICT
);

ALTER TABLE teams
	ADD UNIQUE (name),
	ADD UNIQUE (abbr);
