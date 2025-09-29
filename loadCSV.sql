DROP TABLE IF EXISTS testTable;

CREATE TABLE testTable (
    col1 VARCHAR(255),
    col2 VARCHAR(255)
);

COPY testTable (col1, col2)
FROM 'C:\\Users\\55zay\\Desktop\\CLASSES\\csce331\\test.csv' DELIMITER ',' CSV HEADER;

-- this is a test