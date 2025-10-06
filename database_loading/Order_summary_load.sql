--Prep for update
DROP TABLE IF EXISTS order_summary;

CREATE TABLE order_summary(
    order_key INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_number INT,
    combo_ID INT,
    item_ID INT
);

\copy order_summary FROM 'Csv_files/order_summary.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
--Display changes
SELECT * FROM order_summary;