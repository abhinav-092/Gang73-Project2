DROP TABLE IF EXISTS testtable;

CREATE TABLE testtable(
    col1 VARCHAR,
    col2 VARCHAR
\copy testtable(col1, col2) FROM 'testSheet1.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');
=======
);
>>>>>>> 6f6f08f5bd350a0d08582f0c75b634f266d7d198
