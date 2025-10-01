SELECT
    EXTRACT(MONTH FROM orders.order_date) as order_month,
    EXTRACT(DAY FROM orders.order_date) as order_day,
    SUM(orders.total_price) AS total,
    COUNT(orders.total_price) AS num,
    SUM(orders.total_price) / COUNT(orders.total_price) AS avg
FROM
    orders
WHERE
    EXTRACT(DAY FROM orders.order_date) = :input_day
GROUP BY
    order_day,
    order_month
ORDER BY
    order_day DESC;