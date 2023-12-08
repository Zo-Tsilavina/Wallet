DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts(
     account_id SERIAL PRIMARY KEY,
     name VARCHAR(150),
     amount DOUBLE PRECISION,
     last_update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     transactions_id VARCHAR(200),
     currency_id int REFERENCES currencies(currency_id),
     type VARCHAR(50)
);

INSERT INTO accounts (name, amount, transactions_id, currency_id, type) VALUES ('compte courant', 500, 1, 1, 'Bank');
INSERT INTO accounts (name, amount, transactions_id, currency_id, type) VALUES ('compte epargne', 200, 2, 2, 'Mobile money');