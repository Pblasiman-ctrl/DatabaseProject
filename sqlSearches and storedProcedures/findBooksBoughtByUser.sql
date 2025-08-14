DELIMITER $$

CREATE PROCEDURE FindBooksBoughtByUser(IN inputUserID VARCHAR(200))
BEGIN
    SELECT b.*
    FROM book b
    JOIN buys bu ON b.isbn_13 = bu.isbn_13
    WHERE bu.user_id = inputUserID;
END $$

DELIMITER ;