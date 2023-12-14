DROP TABLE IF EXISTS transfer_history;
CREATE TABLE transfer_history (
    transfer_history_id INT PRIMARY KEY,
    debtor_transaction_id INT NOT NULL,
    creditor_transaction_id INT NOT NULL,
    transfer_date TIMESTAMP
);