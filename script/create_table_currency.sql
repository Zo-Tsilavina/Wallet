DROP TABLE IF EXISTS currency;
CREATE TABLE currency(
     currency_id SERIAL PRIMARY KEY,
     name VARCHAR(50),
     symbol VARCHAR(50)
);

INSERT INTO currency(name, symbole) VALUES ("EUR - euro", "€");
INSERT INTO currency(name, symbole) VALUES ("Dollar - Dollar", "$");