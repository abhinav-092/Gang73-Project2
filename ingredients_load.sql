DROP TABLE IF EXISTS ingredients;

CREATE TABLE ingredients(
    Ingredient_holder_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Ingredient_ID INT,
    item_ID INT,
    Amount_used FLOAT,
    Unit VARCHAR(10)
);

\copy ingredients FROM 'ingredients.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
SELECT * FROM ingredients;