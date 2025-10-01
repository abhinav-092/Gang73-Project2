SELECT
    EXTRACT(MONTH FROM orders.order_date) as order_month, --for display only
    EXTRACT(DAY FROM orders.order_date) as order_day, --for checks against input variable
    SUM(orders.total_price) AS total,
    COUNT(orders.total_price) AS num,
    SUM(orders.total_price) / COUNT(orders.total_price) AS avg --calculated average total
FROM
    orders
WHERE
    EXTRACT(DAY FROM orders.order_date) = :input_day --input given for the specific day of the month
GROUP BY
    order_day,
    order_month
ORDER BY
    order_day DESC;