WITH performance AS ( --finding top performance employee
    SELECT 
        employee_ID, 
        SUM(total_price) AS perf --ordering by revenue
    FROM orders
    GROUP BY employee_ID
    ORDER BY perf DESC
    LIMIT 1
)
SELECT --displaying with extra information
    e.employee_name,
    e.phone_number,
    p.perf
FROM employees e
JOIN performance p ON e.employee_id = p.employee_id;