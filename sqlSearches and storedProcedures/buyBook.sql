DELIMITER $$

CREATE PROCEDURE BuyBook(IN in_isbn_13 VARCHAR(200), IN inputUserID VARCHAR(200))
BEGIN
    INSERT INTO buys (isbn_13, user_id)
    VALUES (in_isbn_13, inputUserID);
END $$

DELIMITER ;