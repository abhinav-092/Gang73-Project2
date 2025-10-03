SELECT * FROM inventory WHERE ingredient_ID = :item_id; --before change
--can do negatives for removing items as well
UPDATE inventory SET amount_on_hand = amount_on_hand + :amount WHERE ingredient_ID = :item_id;
SELECT * FROM inventory WHERE ingredient_ID = :item_id; --after change