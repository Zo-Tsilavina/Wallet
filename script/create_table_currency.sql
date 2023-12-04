DROP TABLE IF EXISTS currency;
CREATE TABLE currency(
     currency_id SERIAL PRIMARY KEY,
     name VARCHAR(50),
     symbol VARCHAR(50)
);

INSERT INTO currency(name, symbol) VALUES ('EUR - euro', '€');
INSERT INTO currency(name, symbol) VALUES ('Dollar - Dollar', '$');