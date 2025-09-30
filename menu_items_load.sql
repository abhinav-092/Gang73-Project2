DROP TABLE IF EXISTS menu_items;

CREATE TABLE menu_items(
    item_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Item_name VARCHAR(120),
    Base_price FLOAT DEFAULT 0,
    Category VARCHAR(25)
);

\copy menu_items FROM 'Project 2 Database - Menu.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
SELECT * FROM menu_items;