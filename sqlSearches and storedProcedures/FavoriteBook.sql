DELIMITER $$

CREATE PROCEDURE FavoriteBook(IN in_isbn_13 VARCHAR(200), IN inputUserID VARCHAR(200))
BEGIN
    INSERT INTO favoriteOf (isbn_13, user_id)
    VALUES (in_isbn_13, inputUserID);
END $$

DELIMITER ;