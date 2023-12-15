DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions(
     transaction_id INT PRIMARY KEY,
     label VARCHAR(50),
     value DOUBLE PRECISION,
     date_time_transaction TIMESTAMP,
     transaction_category_id INT
);

INSERT INTO transactions (transaction_id, label, value,date_time_transaction, transaction_category_id) VALUES (1, 'salaire', 100000.00, '2023-12-01 12:15:00', 3);
INSERT INTO transactions (transaction_id, label, value,date_time_transaction, transaction_category_id) VALUES (2, 'cadeau de noel', 50000.00, '2023-12-02 02:00:00', 2);
INSERT INTO transactions (transaction_id, label, value,date_time_transaction, transaction_category_id) VALUES (3, 'nouvelle chaussure', 20000.00, '2023-12-06 04:00:00', 1);