DROP TABLE IF EXISTS inventory;

CREATE TABLE inventory(
    Ingredient_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Ingredient_name VARCHAR(64),
    Amount_on_hand FLOAT,
    Amount_required FLOAT,
    Unit VARCHAR(10),
    Price_per_unit FLOAT
);

\copy inventory FROM 'inventory.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
SELECT * FROM inventory;