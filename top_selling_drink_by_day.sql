SELECT m."Item_ID",
       m."Name",
       COUNT(*) AS units_sold
FROM order_summary os
JOIN orders o ON o."Order_Number" = os."Order_Number"
JOIN menu   m ON m."Item_ID"      = os."Item_ID"
WHERE o."Date" = :target_date
GROUP BY m."Item_ID", m."Name"
ORDER BY units_sold DESC
