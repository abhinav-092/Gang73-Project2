SELECT o.employee_ID,
       e.employee_name AS employee_name,
       SUM(o.total_price) AS employee_revenue
FROM orders o
LEFT JOIN employees e ON e.employee_ID = o.employee_ID
GROUP BY o.employee_ID, e.employee_name
ORDER BY employee_revenue DESC;
