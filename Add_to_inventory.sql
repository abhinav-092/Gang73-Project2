SELECT * FROM inventory WHERE ingredient_ID = :item_id;
UPDATE inventory SET amount_on_hand = amount_on_hand + :amount WHERE ingredient_ID = :item_id;
SELECT * FROM inventory WHERE ingredient_ID = :item_id;