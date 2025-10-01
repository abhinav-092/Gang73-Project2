WITH daily_totals AS (
  SELECT
    order_date::date AS day,
    SUM(total_price) AS total_sales
  FROM
    orders
  GROUP BY
    order_date::date
)
SELECT
  day,
  total_sales
FROM
  daily_totals
ORDER BY
  total_sales DESC
LIMIT 10;