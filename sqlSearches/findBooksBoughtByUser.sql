DELIMITER $$

CREATE PROCEDURE FindBooksBoughtByUser(IN inputUserID VARCHAR(200))
BEGIN
    SELECT b.*
    FROM book b
    JOIN buys bu ON b.ibsn_13 = bu.ibsn_13
    WHERE bu.user_id = inputUserID;
END $$

DELIMITER ;