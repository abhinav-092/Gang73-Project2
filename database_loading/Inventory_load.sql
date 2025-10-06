--Update for changes
DROP TABLE IF EXISTS inventory;

CREATE TABLE inventory(
    ingredient_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ingredient_name VARCHAR(64),
    amount_on_hand FLOAT,
    amount_required FLOAT,
    unit VARCHAR(10),
    price_per_unit FLOAT
);

\copy inventory FROM 'Csv_files/inventory.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
--Display changes
SELECT * FROM inventory;