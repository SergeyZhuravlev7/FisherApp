-- Тестовые таблицы
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS achievements;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_achievements;

CREATE TABLE IF NOT EXISTS users(id INT AUTO_INCREMENT,
login VARCHAR(30) CHECK(LENGTH(login) > 7 & LENGTH(login) < 31) UNIQUE NOT NULL,
password_hash VARCHAR(255),
name VARCHAR(30) check(LENGTH(name) > 2 & LENGTH(name) < 31),
birthdate DATE,
gender ENUM('MALE', 'FEMALE'),
created_at DATETIME,
email VARCHAR(40),
role VARCHAR(30),
PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS achievements(id SMALLINT AUTO_INCREMENT,
name VARCHAR (60),
description varchar (100),
PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS posts (id INT AUTO_INCREMENT,
user_id INT,
fish VARCHAR(15),
fish_weight DECIMAL(4,2),
message VARCHAR(300),
created_at DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL);

CREATE TABLE IF NOT EXISTS likes (id INT AUTO_INCREMENT,
user_id INT,
post_id INT,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES users(id),
FOREIGN KEY (post_id) REFERENCES posts(id));

CREATE TABLE IF NOT EXISTS user_achievements(user_id INT,
achievement_id SMALLINT,
PRIMARY KEY (user_id, achievement_id));

