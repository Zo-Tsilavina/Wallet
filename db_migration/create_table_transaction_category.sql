DROP TABLE IF EXISTS transaction_categories;
CREATE TABLE transaction_categories(
     transaction_category_id INT PRIMARY KEY,
     name VARCHAR(50),
     type VARCHAR(50)
);

INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (1, 'Nourritures & boissons', 'debit');
INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (2, 'Achats', 'debit');
INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (3, 'Logement', 'debit');
INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (4, 'Transport', 'debit');
INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (5, 'Vehicule', 'debit');
INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (6, 'Loisirs', 'debit');
INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (7, 'Multimedia, PC', 'debit');
INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (8, 'Frais financiers', 'debit');
INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (9, 'Investissement', 'debit');
INSERT INTO transaction_categories (transaction_category_id, name, type) VALUES (10, 'Revenu', 'credit');