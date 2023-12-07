DROP TABLE IF EXISTS currency;
CREATE TABLE currency(
     currency_id SERIAL PRIMARY KEY,
     name VARCHAR(50),
     code VARCHAR(50)
);

INSERT INTO currency(name, code) VALUES ('EURO', 'EUR');
INSERT INTO currency(name, code) VALUES ('Dollar', 'USD');