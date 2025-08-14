DELIMITER $$

CREATE PROCEDURE SearchBooksByCategoryID(IN inputCategoryID INT)
BEGIN
    SELECT b.*
    FROM book b
    JOIN categoryOf c ON b.isbn_13 = c.isbn_13
    WHERE c.category_id = inputCategoryID;
END $$

DELIMITER ;
