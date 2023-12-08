DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions(
     transaction_id SERIAL PRIMARY KEY,
     label VARCHAR(50),
     value DOUBLE PRECISION,
     date_time_transaction TIMESTAMP,
     type_transaction VARCHAR(50)
);

INSERT INTO transactions (label, value,date_time_transaction, type_transaction) VALUES ('salaire', 100000.00, '2023-12-01 12:15:00', 'credit');
INSERT INTO transactions (label, value,date_time_transaction, type_transaction) VALUES ('cadeau de noel', 50000.00, '2023-12-02 02:00:00', 'debit');
INSERT INTO transactions (label, value,date_time_transaction, type_transaction) VALUES ('nouvelle chaussure', 20000.00, '2023-12-06 04:00:00', 'debit');