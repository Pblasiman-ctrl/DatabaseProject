DELIMITER $$

CREATE PROCEDURE FindUsersFavorites(IN inputUserID VARCHAR(200))
BEGIN
    SELECT b.*
    FROM book b
    JOIN favoriteOf f ON b.isbn_13 = f.isbn_13
    WHERE f.user_id = inputUserID;
END $$

DELIMITER ;