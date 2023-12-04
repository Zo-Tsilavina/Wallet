DROP TABLE IF EXISTS transaction;
CREATE TABLE transaction(
     transaction_id SERIAL PRIMARY,
     value DOUBLE PRECISION,
     description text,
     account_id INTEGER REFERENCES accounts(account_id),
     date_transaction TIMESTAMP  DEFAULT current_timestamp,
);

INSERT INTO transaction (value, account_id, currency_id) VALUES (10, transfer, 1);
INSERT INTO transaction (value, account_id, currency_id) VALUES (50, deposit, 2);