WITH performance AS (
    SELECT 
        Employee_ID, 
        SUM(total_price) AS perf
    FROM orders
    GROUP BY Employee_ID
    ORDER BY perf DESC
    LIMIT 1
)
SELECT 
    e.employee_name,
    e.phone_number,
    p.perf
FROM employees e
JOIN performance p ON e.employee_id = p.employee_id;