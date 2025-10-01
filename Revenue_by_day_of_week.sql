SELECT
    TO_CHAR(orders.order_date, 'Day') as order_day,
    SUM(orders.Total_price) AS total,
    COUNT(orders.Total_price) AS num,
    SUM(orders.Total_price) / COUNT(orders.Total_price) AS avg
FROM
    orders
WHERE
    TRIM(TO_CHAR(orders.order_date, 'Day')) = :'input_day'
GROUP BY
    order_day
ORDER BY
    order_day DESC;