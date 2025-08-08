DELIMITER $$

CREATE PROCEDURE SearchBooksByAuthorName(IN inputAuthorName VARCHAR(2000))
BEGIN
    SELECT b.*
    FROM book b
    JOIN writes w ON b.ibsn_13 = w.ibsn_13
    JOIN author a ON w.author_id = a.author_id
    WHERE a.author_name LIKE CONCAT('%', inputAuthorName, '%');
END $$

DELIMITER ;