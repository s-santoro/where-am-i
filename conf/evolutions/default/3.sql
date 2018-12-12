-- # Sessions schema

# --- !Ups

DROP TABLE IF EXISTS sessions CASCADE;
CREATE TABLE sessions (
    id SERIAL NOT NULL,
    user_fk SERIAL REFERENCES users(id),
    location_fk SERIAL REFERENCES locations(id),
    score INT NOT NULL,
    timestamp VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE IF EXISTS sessions;