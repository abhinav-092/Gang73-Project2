-- find ingredients that are below the required amount and need to be ordered
SELECT
  ingredient_name,
  amount_on_hand,
  amount_required,
  (amount_required - amount_on_hand) AS qty_to_order, 
  -- calculate total cost for the order
  ROUND(((amount_required - amount_on_hand) * price_per_unit)::numeric, 2) AS total_order_cost
FROM inventory
WHERE amount_on_hand < amount_required;