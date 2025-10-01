WITH item_counts AS (
  SELECT
      EXTRACT(DOW FROM o.order_date)::int AS day_num,          -- 0=Sun ... 6=Sat
      TRIM(TO_CHAR(o.order_date, 'Day')) AS day_name,          -- weekday name
      m.item_id,
      m.item_name,
      COUNT(*) AS units_sold,
      SUM(m.base_price) AS total_revenue
  FROM order_summary os
  JOIN orders      o ON o.order_number = os.order_number
  JOIN menu_items  m ON m.item_id      = os.item_id
  WHERE m.category ILIKE 'drink-%'
  GROUP BY day_num, day_name, m.item_id, m.item_name
),
ranked AS (
  SELECT
      day_num, day_name, item_id, item_name,
      units_sold, total_revenue,
      ROW_NUMBER() OVER (
          PARTITION BY day_num
          ORDER BY units_sold DESC, total_revenue DESC
      ) AS r
  FROM item_counts
)
SELECT
  day_name,
  item_id,
  item_name,
  units_sold,
  total_revenue
FROM ranked
WHERE r = 1
ORDER BY day_num;

