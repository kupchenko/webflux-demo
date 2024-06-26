drop table if exists orders;
drop table if exists clients;
drop table if exists addresses;

CREATE TABLE IF NOT EXISTS addresses
(
    id       INT NOT NULL AUTO_INCREMENT,
    street   VARCHAR(255),
    postcode VARCHAR(255),
    city     VARCHAR(255),
    country  VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS clients
(
    id         INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    telephone  VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS orders
(
    id          INT NOT NULL AUTO_INCREMENT,
    pilotes     int,
    order_total double,
    client_fk   INTEGER REFERENCES clients (id),
    address_fk  INTEGER REFERENCES addresses (id),
    created_at  timestamp,
    PRIMARY KEY (id)
);
