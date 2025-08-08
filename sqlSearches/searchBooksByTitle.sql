DELIMITER $$

CREATE PROCEDURE SearchBooksByTitle(IN inputTitle VARCHAR(2000))
BEGIN
    SELECT * FROM book WHERE title LIKE CONCAT('%', inputTitle, '%');
END $$

DELIMITER ;