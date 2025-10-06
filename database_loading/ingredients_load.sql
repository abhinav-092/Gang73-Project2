--Update table to latest info
DROP TABLE IF EXISTS ingredients;

CREATE TABLE ingredients(
    ingredient_holder_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ingredient_ID INT,
    item_ID INT,
    amount_used FLOAT,
    unit VARCHAR(10)
);

\copy ingredients FROM 'Csv_files/ingredients.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
--Display changes
SELECT * FROM ingredients;