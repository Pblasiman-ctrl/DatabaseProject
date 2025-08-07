DELIMITER $$

CREATE PROCEDURE SearchBooksByCategoryID(IN inputCategoryID INT)
BEGIN
    SELECT b.*
    FROM book b
    JOIN categoryOf c ON b.ibsn_13 = c.ibsn_13
    WHERE c.category_id = inputCategoryID;
END $$

DELIMITER ;