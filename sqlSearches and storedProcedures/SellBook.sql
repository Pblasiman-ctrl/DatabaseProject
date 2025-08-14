DELIMITER //

CREATE PROCEDURE SellBook(
    IN in_isbn_13 VARCHAR(13),
    IN in_user_id VARCHAR(255)
)
BEGIN
    DELETE FROM buys
    WHERE isbn_13 = in_isbn_13 AND user_id = in_user_id;
END //

DELIMITER ;