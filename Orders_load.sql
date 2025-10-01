DROP TABLE IF EXISTS orders;

CREATE TABLE orders(
    order_number INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_date DATE,
    order_time TIME,
    total_price FLOAT,
    employee_ID INT
);

\copy orders FROM 'orders.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
SELECT * FROM orders;