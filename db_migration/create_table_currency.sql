DROP TABLE IF EXISTS currency;
CREATE TABLE currency(
     id INT PRIMARY KEY,
     name VARCHAR(50),
     code VARCHAR(50)
);
INSERT INTO currency (id, name, code) VALUES (1, 'Ariary', 'MGA');
INSERT INTO currency (id, name, code) VALUES (2, 'EURO', 'EUR');
