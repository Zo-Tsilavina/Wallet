DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts(
     accounts_id SERIAL PRIMARY KEY,
     name VARCHAR(150),
     currency_id int REFERENCES currency(currency_id)
);

INSERT INTO accounts (name, currency_id) VALUES (BMOI, 2);
INSERT INTO accounts (name, currency_id) VALUES (BOA, 1);