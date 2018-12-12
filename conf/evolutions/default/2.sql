-- # Users schema

# --- !Ups

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    id SERIAL NOT NULL,
    username VARCHAR(30) NOT NULL,
    password TEXT NOT NULL,
    salt TEXT NOT NULL,
    avatar BIGINT NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE IF EXISTS users;