DELIMITER //

CREATE PROCEDURE CanSellBook(
    IN in_isbn_13 VARCHAR(13),
    IN in_user_id VARCHAR(255),
    OUT can_sell BOOLEAN
)
BEGIN
    DECLARE record_count INT;

    SELECT COUNT(*) INTO record_count
    FROM buys
    WHERE isbn_13 = in_isbn_13 AND user_id = in_user_id;

    SET can_sell = (record_count > 0);
END //

DELIMITER ;