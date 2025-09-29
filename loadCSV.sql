DROP TABLE IF EXISTS testTable;

CREATE TABLE IF NOT EXISTS testing(
    test1 VARCHAR(255),
    test2 VARCHAR(255),
    test3 VARCHAR(255)
);

COPY testing (test1, test2, test3)
FROM 'C:\\Users\\55zay\\Desktop\\CLASSES\\csce331\\test.csv' DELIMITER ',' CSV HEADER;