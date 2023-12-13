DROP TABLE IF EXISTS transfer_history;
CREATE TABLE transfer_history (
    transfer_history_id SERIAL PRIMARY KEY,
    debtor_transaction_id int NOT NULL,
    creditor_transaction_id int NOT NULL,
    transfer_date TIMESTAMP
);