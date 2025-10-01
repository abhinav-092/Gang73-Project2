SELECT * FROM inventory WHERE Ingredient_ID = :item_id;
UPDATE inventory SET amount_on_hand = amount_on_hand + :amount WHERE Ingredient_ID = :item_id;
SELECT * FROM inventory WHERE Ingredient_ID = :item_id;