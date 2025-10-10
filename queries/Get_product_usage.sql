\pset pager off
\timing on
\echo Running inventory usage from :'start_date' to :'end_date' (inclusive)

WITH window_orders AS (
  SELECT o.order_number
  FROM orders o
  WHERE o.order_date >= (:'start_date')::date
    AND o.order_date <  ((:'end_date')::date + INTERVAL '1 day')
)
SELECT
  inv.ingredient_id,
  inv.ingredient_name,
  COALESCE(inv.unit, ing.unit) AS unit,
  SUM(ing.amount_used) AS total_amount_used,
  SUM(ing.amount_used * inv.price_per_unit) AS estimated_cost_used
FROM window_orders w
JOIN order_summary os ON os.order_number = w.order_number
JOIN ingredients    ing ON ing.item_id = os.item_id
JOIN inventory      inv ON inv.ingredient_id = ing.ingredient_id
GROUP BY inv.ingredient_id, inv.ingredient_name, COALESCE(inv.unit, ing.unit)
ORDER BY inv.ingredient_name;