DROP TABLE IF EXISTS currency;
CREATE TABLE currencies(
     currency_id SERIAL PRIMARY KEY,
     name VARCHAR(50),
     code VARCHAR(50)
);

INSERT INTO currencies (name, code) VALUES ('EURO', 'EUR');
INSERT INTO currencies (name, code) VALUES ('Dollar', 'USD');