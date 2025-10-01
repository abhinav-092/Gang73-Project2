WITH top10 AS (
  SELECT total_price
  FROM orders
  WHERE order_date = :'target_date'::date
  ORDER BY total_price DESC
  LIMIT 10
)
SELECT COALESCE(SUM(total_price), 0) AS top10_sum
FROM top10;
