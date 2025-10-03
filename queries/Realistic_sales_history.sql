SELECT
    EXTRACT(HOUR FROM order_time) AS hour, 
    COUNT(orders.order_number) AS number_of_orders, --counting orders per hour
    SUM(orders.total_price) AS total --adding totals within the hour
FROM
    orders
GROUP BY
    hour --displays by hour
ORDER BY
    number_of_orders DESC;