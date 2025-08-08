DELIMITER $$

CREATE PROCEDURE FindUsersFavorites(IN inputUserID VARCHAR(200))
BEGIN
    SELECT b.*
    FROM book b
    JOIN favoriteOf f ON b.ibsn_13 = f.ibsn_13
    WHERE f.user_id = inputUserID;
END $$

DELIMITER ;