DROP TABLE IF EXISTS currencies;
CREATE TABLE currencies(
     currency_id INT PRIMARY KEY,
     name VARCHAR(50),
     code VARCHAR(50)
);
INSERT INTO currencies (currency_id, name, code) VALUES (1, 'Ariary', 'MGA');
INSERT INTO currencies (currency_id, name, code) VALUES (2, 'EURO', 'EUR');
