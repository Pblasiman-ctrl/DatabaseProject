DELIMITER $$

CREATE PROCEDURE SearchBooksMultiFilter (
    IN inputTitle VARCHAR(2000),
    IN inputAuthorName VARCHAR(1000),
    IN inputCategoryName  VARCHAR(2000),
    IN inputIsbn BIGINT,
    IN inputFormat VARCHAR(2000)
)
BEGIN
    SELECT DISTINCT b.*
    FROM book b
    LEFT JOIN author a ON b.author_id = a.author_id
    LEFT JOIN format f ON f.format_id = b.format_id
    LEFT JOIN category c ON b.category_id = c.category_id
    WHERE (inputTitle IS NULL OR TRIM(inputTitle) = '' OR b.title LIKE CONCAT('%', inputTitle, '%'))
      AND (inputAuthorName IS NULL OR TRIM(inputAuthorName) = '' OR a.author_name LIKE CONCAT('%', inputAuthorName, '%'))
      AND (inputCategoryName IS NULL OR TRIM(inputCategoryName) = '' OR c.category_name LIKE CONCAT('%', inputCategoryName, '%'))
      AND (inputFormat IS NULL OR TRIM(inputFormat) = '' OR f.format_name LIKE CONCAT('%', inputFormat, '%'))
      AND (inputIsbn IS NULL OR b.ibsn_13 = inputIsbn);
END $$

DELIMITER ;