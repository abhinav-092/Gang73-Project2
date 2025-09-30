DROP TABLE IF EXISTS employees;

CREATE TABLE employees(
    Employee_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Employee_name VARCHAR(120),
    Phone_number VarChar(10),
    Is_manager BOOLEAN
);

\copy employees FROM 'Project 2 Database - Employees.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
SELECT * FROM employees;