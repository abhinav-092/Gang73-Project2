DROP TABLE IF EXISTS employees;

CREATE TABLE employees(
    employee_ID INT PRIMARY KEY,
    employee_name VARCHAR(120),
    phone_number VarChar(10),
    is_manager BOOLEAN
);

\copy employees FROM 'employees.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
SELECT * FROM employees;