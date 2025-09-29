DROP TABLE IF EXISTS testtable;

CREATE TABLE testtable(
    col1 VARCHAR,
    col2 VARCHAR
<<<<<<< HEAD
);

\copy testtable(col1, col2) FROM 'testSheet1.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
=======
);
>>>>>>> abhinav_test
