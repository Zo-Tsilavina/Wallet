DROP TABLE IF EXISTS transaction_categories;
CREATE TABLE transaction_categories(
     transaction_category_id INT PRIMARY KEY,
     name VARCHAR(50),
     type VARCHAR(50)
);