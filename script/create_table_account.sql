DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts(
     account_id SERIAL PRIMARY KEY,
     name VARCHAR(150),
     amount DOUBLE PRECISION,
     last_update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     transaction_id int REFERENCES transaction(transaction_id),
     currency_id int REFERENCES currency(currency_id),
     type VARCHAR(50)
);

INSERT INTO accounts (name, amount, transaction_id, currency_id, type) VALUES ('compte courant', 500, 1, 1, 'Bank');
INSERT INTO accounts (name, amount, transaction_id, currency_id, type) VALUES ('compte epargne', 200, 2, 2, 'Mobile money');