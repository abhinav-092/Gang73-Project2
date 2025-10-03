SELECT o.employee_ID, 
       e.employee_name AS employee_name,
       SUM(o.total_price) AS employee_revenue --selecting required fields like employee ID, name and revenue
FROM orders o
LEFT JOIN employees e ON e.employee_ID = o.employee_ID --joining tables orders and employees on employee ID
GROUP BY o.employee_ID, e.employee_name --adding group by clause as aggregate function is used
ORDER BY employee_revenue DESC;
