DROP TABLE IF EXISTS orders;

CREATE TABLE orders(
    Order_number INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_date DATE,
    order_time TIME,
    Total_price FLOAT,
    Employee_ID INT
);

\copy orders FROM 'Project 2 Database - Orders.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
SELECT * FROM orders;