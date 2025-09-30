SELECT Employee_name, Phone_number 
FROM employees 
WHERE Employee_ID = (
    SELECT Employee_ID FROM (
        SELECT Employee_ID, SUM(orders.Total_price) AS perf 
        FROM orders 
        GROUP BY Employee_ID 
        ORDER BY perf DESC 
        LIMIT 1) AS somename
    );
