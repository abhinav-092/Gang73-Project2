WITH performance AS (
    SELECT 
        employee_ID, 
        SUM(total_price) AS perf
    FROM orders
    GROUP BY employee_ID
    ORDER BY perf DESC
    LIMIT 1
)
SELECT 
    e.employee_name,
    e.phone_number,
    p.perf
FROM employees e
JOIN performance p ON e.employee_id = p.employee_id;