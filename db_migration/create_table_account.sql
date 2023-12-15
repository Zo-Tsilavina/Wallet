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
INSERT INTO accounts (account_id, name, amount, transactions_id, currency_id, type) VALUES (1, 'Compte courant', 5000.00, '1', 1, 'Courant');
INSERT INTO accounts (account_id, name, amount, transactions_id, currency_id, type) VALUES (2, 'Epargne', 10000.00, '2', 2, 'Epargne');
INSERT INTO accounts (account_id, name, amount, transactions_id, currency_id, type) VALUES (3, 'Compte entreprise', 7500.00, '3', 1, 'Entreprise');
INSERT INTO accounts (account_id, name, amount, transactions_id, currency_id, type) VALUES (4, 'Compte investissement', 20000.00, '2', 2, 'Investissement')