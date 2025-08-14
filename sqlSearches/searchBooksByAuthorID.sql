DELIMITER $$

CREATE PROCEDURE SearchBooksByAuthorID(IN inputAuthorID BIGINT)
BEGIN
    SELECT b.*
    FROM book b
    JOIN writes w ON b.isbn_13 = w.isbn_13
    WHERE w.author_id = inputAuthorID;
END $$

DELIMITER ;
