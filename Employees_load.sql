DROP TABLE IF EXISTS employees;

CREATE TABLE employees(
    Employee_ID INT PRIMARY KEY,
    Employee_name VARCHAR(120),
    Phone_number VarChar(10),
    Is_manager BOOLEAN
);

\copy employees FROM 'employees.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
SELECT * FROM employees;