-- add default items

# --- !Ups

INSERT INTO locations(imagekey) VALUES ('6U_0x-ja_iEl2fg4SktQcA');
INSERT INTO locations(imagekey) VALUES ('K-ZBSAgzugxFfwOslGlJ2A');
INSERT INTO locations(imagekey) VALUES ('Ciz0DtWxNISkyf5V2lLkHg');
INSERT INTO locations(imagekey) VALUES ('K0wTx8_4uPUE7rI9pICEVQ');
INSERT INTO locations(imagekey) VALUES ('9N5tWOXqNIRiGyOHOdm94A');
INSERT INTO locations(imagekey) VALUES ('P2aTYLTnnoNReq5Zaqr7Xw');
INSERT INTO locations(imagekey) VALUES ('mi9nsRI68oC4QI4AR4hi-Q');
INSERT INTO locations(imagekey) VALUES ('vkm9XAD97AdvB6TQHzpaXA');
INSERT INTO locations(imagekey) VALUES ('koOj0hylfl9x_rKkuthB7A');
INSERT INTO locations(imagekey) VALUES ('K79OhrCeRIyvNDvrvSlhIw');
INSERT INTO locations(imagekey) VALUES ('cJU16qN9LO_zu03AdeH_Lw');
INSERT INTO locations(imagekey) VALUES ('XikSmqSsrzorADI23EhokA');
INSERT INTO locations(imagekey) VALUES ('ydJj48F76qIyNzpJPxHPow');
INSERT INTO locations(imagekey) VALUES ('lQM6R8YgfWNCLfJqPsiXVw');
INSERT INTO locations(imagekey) VALUES ('M1DBpuPUuT3hFSaYP1nJFg');
INSERT INTO locations(imagekey) VALUES ('1bFvEIABer70xwNHL4D48w');
INSERT INTO locations(imagekey) VALUES ('Kdm4Y6_SdHiO74VQfclXOQ');
INSERT INTO locations(imagekey) VALUES ('me-Chk9pEnzMe3Mjal8aDA');
INSERT INTO locations(imagekey) VALUES ('7BU407LmfPcIomVTodooYg');
INSERT INTO locations(imagekey) VALUES ('ehxIn4b3tDKEmuGAhoDCeg');
INSERT INTO locations(imagekey) VALUES ('5vjXTuoP6CdKPom8gKDzkQ');
INSERT INTO locations(imagekey) VALUES ('gEuzzxRHlXw_P4LP2CaDyA');
INSERT INTO locations(imagekey) VALUES ('k0eHRLFsDXnINQwPm_1oDg');
INSERT INTO locations(imagekey) VALUES ('IoWjtCPrx2tzrNDTARGwWA');
INSERT INTO locations(imagekey) VALUES ('hyjmvzvEm7MGKzCJaKGj6A');
INSERT INTO locations(imagekey) VALUES ('STwJ_hKvJsDZ_MEgQH2XfQ');
INSERT INTO locations(imagekey) VALUES ('1YEqQcN7pONkbCRRcBu2EA');
INSERT INTO locations(imagekey) VALUES ('TxzJgZH24yki1PksfJM_0g');
INSERT INTO locations(imagekey) VALUES ('bcyKs0sEZZiV-1e9ff7BDw');
INSERT INTO locations(imagekey) VALUES ('UXfj9tAg2fm5jJftakYaew');
INSERT INTO locations(imagekey) VALUES ('S1nT1eUMAyMhti2umfHj5A');
INSERT INTO locations(imagekey) VALUES ('osFubMJwhWGpKJxm1gkZDg');

INSERT INTO users(username, password, salt, avatar) VALUES ('bigmac_kritiker','keinbigmac','salt',4);
INSERT INTO users(username, password, salt, avatar) VALUES ('giandamme','test123','salt',1);
INSERT INTO users(username, password, salt, avatar) VALUES ('kylian_Mmappe','password123','salt',2);
INSERT INTO users(username, password, salt, avatar) VALUES ('sandrohne','pilota','salt',3);

INSERT INTO sessions(user_fk, location_fk, score, timestamp) VALUES (1, 1, 9853, '2018-10-21');
INSERT INTO sessions(user_fk, location_fk, score, timestamp) VALUES (1, 4, 54232, '2018-10-21');
INSERT INTO sessions(user_fk, location_fk, score, timestamp) VALUES (1, 2, 139, '2018-10-22');
INSERT INTO sessions(user_fk, location_fk, score, timestamp) VALUES (1, 3, 9853, '2018-10-22');
INSERT INTO sessions(user_fk, location_fk, score, timestamp) VALUES (1, 4, 98434, '2018-10-23');
INSERT INTO sessions(user_fk, location_fk, score, timestamp) VALUES (1, 2, 5234, '2018-10-24');
INSERT INTO sessions(user_fk, location_fk, score, timestamp) VALUES (2, 3, 123, '2018-11-08');
INSERT INTO sessions(user_fk, location_fk, score, timestamp) VALUES (4, 4, 420, '2018-11-10');

# --- !Downs
