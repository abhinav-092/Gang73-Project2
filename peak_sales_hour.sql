SELECT
  EXTRACT(HOUR FROM order_time)::int AS hour_local,
  COUNT(*) AS orders
FROM orders
GROUP BY 1
ORDER BY orders DESC, hour_local ASC
LIMIT 1;