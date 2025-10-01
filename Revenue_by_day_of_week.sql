WITH daily_revenue AS (
    SELECT
        DATE(order_date) AS order_date,
        TRIM(TO_CHAR(order_date, 'Day')) AS order_day,
        SUM(total_price) AS day_total
    FROM orders
    GROUP BY order_date
)
SELECT
    order_day,
    AVG(day_total) AS avg_revenue_per_day
FROM daily_revenue
GROUP BY order_day
ORDER BY order_day;
