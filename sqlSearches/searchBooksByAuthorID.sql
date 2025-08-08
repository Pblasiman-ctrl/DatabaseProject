DELIMITER $$

CREATE PROCEDURE SearchBooksByAuthorID(IN inputAuthorID BIGINT)
BEGIN
    SELECT b.*
    FROM book b
    JOIN writes w ON b.ibsn_13 = w.ibsn_13
    WHERE w.author_id = inputAuthorID;
END $$

DELIMITER ;