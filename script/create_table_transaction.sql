DROP TABLE IF EXISTS transaction;
CREATE TABLE transactions(
     transaction_id SERIAL PRIMARY KEY,
     label VARCHAR(50),
     value DOUBLE PRECISION,
     date_time_transaction TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     type_transaction VARCHAR(50)
);

INSERT INTO transactions (label, value, type_transaction) VALUES ('pret bancaire', 50, 'debit');
INSERT INTO transactions (label, value, type_transaction) VALUES ('pret bancaire', 100,'credit');