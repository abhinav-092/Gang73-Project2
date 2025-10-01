DROP TABLE IF EXISTS order_summary;

CREATE TABLE order_summary(
    Order_key INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Order_number INT,
    Combo_ID INT,
    item_ID INT
);

\copy order_summary FROM 'order_summary.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
SELECT * FROM order_summary;