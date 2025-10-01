SELECT
  date_trunc('week', order_date::timestamp)::date AS week_start,
  COUNT(*) AS orders
FROM orders
GROUP BY 1
ORDER BY 1;