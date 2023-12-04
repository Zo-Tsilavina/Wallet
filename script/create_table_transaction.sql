DROP TABLE IF EXISTS transaction;
CREATE TABLE transaction(
     transaction_id SERIAL PRIMARY,
     value DOUBLE PRECISION,
     description text,
     account_id INTEGER REFERENCES accounts(account_id),
     type VARCHAR(50),
     date_transaction TIMESTAMP  DEFAULT current_timestamp
);

INSERT INTO transaction (value, description, account_id, type) VALUES (10, "salary", 1, "gain");
INSERT INTO transaction (value,description, account_id, type) VALUES (50, "buy new clothes", 2, "deepens");