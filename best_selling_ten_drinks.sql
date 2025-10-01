SELECT
  mi.item_name,
  COUNT(*) AS sold
FROM order_summary AS os
JOIN menu_items AS mi
  ON mi.item_id = os.item_id
WHERE os.item_id BETWEEN 1 AND 19
GROUP BY mi.item_name
ORDER BY sold DESC, mi.item_name
LIMIT 10;