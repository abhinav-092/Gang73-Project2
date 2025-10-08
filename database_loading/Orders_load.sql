--Prep for update
DROP TABLE IF EXISTS orders;

CREATE TABLE orders(
    order_number INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_date DATE,
    order_time TIME,
    total_price FLOAT,
    employee_ID INT
);

\copy orders FROM 'Csv_files/orders.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');

SELECT setval(
    pg_get_serial_sequence('orders', 'order_number'),
    COALESCE((SELECT MAX(order_number) + 1 FROM orders), 1),
    false
);

--Display changes
SELECT * FROM orders;