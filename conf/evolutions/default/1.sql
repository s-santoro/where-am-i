-- # Locations schema

# --- !Ups

DROP TABLE IF EXISTS locations CASCADE;
CREATE TABLE locations (
    id SERIAL NOT NULL,
    imageKey varchar(22) NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE IF EXISTS locations;