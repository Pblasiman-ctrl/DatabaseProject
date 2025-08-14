DELIMITER //

CREATE PROCEDURE find_reader (
    IN username VARCHAR(200),
    IN password VARCHAR(200),
    OUT message INT
)
BEGIN
    DECLARE user_count INT;

    SELECT COUNT(*) INTO user_count
    FROM reader
    WHERE reader.user_id = username
      AND reader.user_password = password;

    IF user_count = 1 THEN
        SET message = 1;
    ELSE
        SET message = 0;
    END IF;
END;
//

DELIMITER ;