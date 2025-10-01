SELECT Order_Number, Total_price
FROM orders
WHERE Date = :target_date
ORDER BY Total_price DESC
LIMIT 10;
