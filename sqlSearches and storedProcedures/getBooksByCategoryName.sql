DELIMITER $$

CREATE PROCEDURE GetBooksByCategoryName(IN inputCategoryName VARCHAR(2000))
BEGIN
    SELECT b.*
    FROM book b
    JOIN book_category bc ON b.ibsn_13 = bc.ibsn_13
    JOIN category c ON bc.category_id = c.category_id
    WHERE c.category_name LIKE CONCAT('%', inputCategoryName, '%');
END $$

DELIMITER ;