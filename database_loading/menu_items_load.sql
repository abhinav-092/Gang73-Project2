--Update table if needed by remaking
DROP TABLE IF EXISTS menu_items;

CREATE TABLE menu_items(
    item_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_name VARCHAR(120),
    base_price FLOAT DEFAULT 0,
    category VARCHAR(25)
);

\copy menu_items FROM 'Csv_files/menu_items.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
--Display changes
SELECT * FROM menu_items;