WITH target AS (
  SELECT date_trunc('week', :'target_date'::timestamp) AS wk
)
SELECT COUNT(*) AS orders
FROM orders o, target t
WHERE date_trunc('week', (o.order_date::timestamp)) = t.wk;