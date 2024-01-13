DROP TABLE IF EXISTS account;
CREATE TABLE account(
     id INT PRIMARY KEY,
     name VARCHAR(150),
     amount DOUBLE PRECISION,
     last_update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     transactions_id VARCHAR(200),
     currency_id int REFERENCES currency(id),
     type VARCHAR(50)
    );
    INSERT INTO account (id, name, amount, transactions_id, currency_id, type) VALUES (1, 'Compte courant', 5000.00, '1', 1, 'Courant');
    INSERT INTO account (id, name, amount, transactions_id, currency_id, type) VALUES (2, 'Epargne', 10000.00, '2', 2, 'Epargne');
    INSERT INTO account (id, name, amount, transactions_id, currency_id, type) VALUES (3, 'Compte entreprise', 7500.00, '3', 1, 'Entreprise');
    INSERT INTO account (id, name, amount, transactions_id, currency_id, type) VALUES (4, 'Compte investissement', 20000.00, '2', 2, 'Investissement')