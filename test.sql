DROP TABLE IF EXISTS testtable;

CREATE TABLE testtable (
    col1 VARCHAR(100),
    col2 VARCHAR(100)
);

INSERT INTO products (product_name, price)
VALUES
('Deskptop Computer', 800),
('Laptop', 1200),
('Tablet', 200),
('Monitor', 350),
('Printer', 150);

SELECT * FROM products;