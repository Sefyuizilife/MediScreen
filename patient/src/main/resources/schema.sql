CREATE TABLE IF NOT EXISTS patient
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    lastname  VARCHAR(50)  NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    birthdate DATE         NOT NULL,
    gender    CHAR         NOT NULL,
    address   VARCHAR(255) null,
    phone     varchar(20)  null
);
