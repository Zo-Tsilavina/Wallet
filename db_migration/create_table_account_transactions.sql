DROP TABLE IF EXISTS account_transactions;

CREATE TABLE account_transactions (
    account_transaction_id SERIAL PRIMARY KEY,
    account_id INT REFERENCES account(id),
    transaction_id INT REFERENCES "transaction"(id),
    CONSTRAINT unique_account_transaction UNIQUE (account_id, transaction_id)
);