SELECT
  ingredient_name,
  amount_on_hand,
  amount_required,
  (amount_required - amount_on_hand) AS qty_to_order,
  ROUND(((amount_required - amount_on_hand) * price_per_unit)::numeric, 2) AS total_order_cost
FROM inventory
WHERE amount_on_hand < amount_required;