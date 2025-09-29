DROP TABLE IF EXISTS testTable;

CREATE TABLE IF NOT EXISTS testing(
    test1 VARCHAR(255),
    test2 VARCHAR(255),
    test3 VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS testing2(
    test4 VARCHAR(255),
    test5 VARCHAR(255)
);

COPY testing (test1, test2, test3)
FROM 'C:\\Users\\55zay\\Desktop\\CLASSES\\csce331\\test.csv' DELIMITER ',' CSV HEADER;

COPY testing2 (test4, test5)
FROM 'C:\\Users\\55zay\\Desktop\\CLASSES\\csce331\\test2.csv' DELIMITER ',' CSV HEADER;