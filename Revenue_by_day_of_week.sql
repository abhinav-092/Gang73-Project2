WITH daily_revenue AS (
    SELECT
        DATE(order_date) AS order_date,
        TRIM(TO_CHAR(order_date, 'Day')) AS order_day, --finding day of week of the data entry
        SUM(total_price) AS day_total --sum within day
    FROM orders
    GROUP BY order_date
)
SELECT
    order_day,
    AVG(day_total) AS avg_revenue_per_day --for specific day of the week
FROM daily_revenue
GROUP BY order_day
ORDER BY order_day;
