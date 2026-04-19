CREATE TABLE IF NOT EXISTS users (
id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
email VARCHAR(255) NOT NULL,
login VARCHAR(255) NOT NULL,
name VARCHAR(255),
birthday DATE,
UNIQUE (email),
UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS mpa (
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS films (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
description VARCHAR(200),
release_date DATE,
duration BIGINT,
mpa_id INT,
FOREIGN KEY (mpa_id) REFERENCES mpa(id)
);

CREATE TABLE IF NOT EXISTS film_genres (
film_id BIGINT REFERENCES films(id),
genre_id INT REFERENCES genres(id),
PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
user_id BIGINT NOT NULL references users(id),
film_id BIGINT NOT NULL references films(id),
PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS friendships (
user_id BIGINT NOT NULL references users(id),
friend_id BIGINT NOT NULL references users(id),
PRIMARY KEY (user_id, friend_id)
);
