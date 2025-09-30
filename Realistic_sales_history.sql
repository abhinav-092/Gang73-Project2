SELECT
    EXTRACT(HOUR FROM order_time) AS hour,
    COUNT(orders.order_number) AS number_of_orders,
    SUM(orders.total_price) AS total
FROM
    orders
GROUP BY
    hour
ORDER BY
    number_of_orders DESC;