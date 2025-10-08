--Prep for update
DROP TABLE IF EXISTS order_summary;

CREATE TABLE order_summary(
    order_key INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_number INT,
    combo_ID INT,
    item_ID INT
);

\copy order_summary FROM 'Csv_files/order_summary.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');

SELECT setval(
    pg_get_serial_sequence('order_summary', 'order_key'),
    COALESCE((SELECT MAX(order_key) + 1 FROM order_summary), 1),
    false
);

--Display changes
SELECT * FROM order_summary;