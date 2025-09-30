DROP TABLE IF EXISTS order_summary;

CREATE TABLE order_summary(
    Order_key INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Order_number INT,
    Combo_ID INT,
    item_ID INT
);

\copy order_summary FROM 'Project 2 Database - Order Summary.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
SELECT * FROM order_summary;