SELECT o."Employee_ID",
       e."Name" AS employee_name,
       SUM(o."Total_price") AS employee_revenue
FROM orders o
LEFT JOIN employees e ON e."Employee_ID" = o."Employee_ID"
GROUP BY o."Employee_ID", e."Name"
ORDER BY employee_revenue DESC;
