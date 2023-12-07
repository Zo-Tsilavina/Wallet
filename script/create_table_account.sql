DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts(
     accounts_id SERIAL PRIMARY KEY,
     name VARCHAR(150),
     amount DOUBLE PRECISION,
     last_update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     transaction_id int REFERENCES transaction(transaction_id),
     currency_id int REFERENCES currency(currency_id),
     type VARCHAR(50)
);

