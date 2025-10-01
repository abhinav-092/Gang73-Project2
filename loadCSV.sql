DROP TABLE IF EXISTS testTable;

CREATE TABLE testTable (
    col1 VARCHAR(255),
    col2 VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS testing2(
    test4 VARCHAR(255),
    test5 VARCHAR(255)
);

COPY testing (test1, test2, test3)
FROM 'C:\\Users\\55zay\\Desktop\\CLASSES\\csce331\\test.csv' DELIMITER ',' CSV HEADER;

COPY testing2 (test4, test5)
FROM 'C:\\Users\\55zay\\Desktop\\CLASSES\\csce331\\test2.csv' DELIMITER ',' CSV HEADER;
=======
COPY testing (test1, test2, test3)
FROM 'C:\\Users\\55zay\\Desktop\\CLASSES\\csce331\\test.csv' DELIMITER ',' CSV HEADER;

DROP TABLE IF EXISTS pokedex;
>>>>>>> 6f6f08f5bd350a0d08582f0c75b634f266d7d198
