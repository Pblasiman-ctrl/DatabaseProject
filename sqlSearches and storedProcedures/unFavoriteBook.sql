DELIMITER $$

CREATE PROCEDURE UnFavoriteBook(IN in_isbn_13 VARCHAR(200), IN inputUserID VARCHAR(200))
BEGIN
    DELETE FROM favoriteOf
    WHERE in_isbn_13 = isbn_13 AND inputUserID = user_id;
END $$

DELIMITER ;