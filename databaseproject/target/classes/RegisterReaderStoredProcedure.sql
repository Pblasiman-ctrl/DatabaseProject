DELIMITER //

CREATE PROCEDURE register_reader (
    IN username VARCHAR(200),
    IN password VARCHAR(200)
)
BEGIN
        INSERT INTO reader (user_id, user_password)
        VALUES (username, password);
END;
//

DELIMITER ;