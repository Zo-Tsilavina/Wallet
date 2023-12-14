DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts(
     account_id INT PRIMARY KEY,
     name VARCHAR(150),
     amount DOUBLE PRECISION,
     last_update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     transactions_id VARCHAR(200),
     currency_id int REFERENCES currencies(currency_id),
     type VARCHAR(50)
);
