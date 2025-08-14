DELIMITER $$

CREATE PROCEDURE SearchBooksMultiFilter (
    IN inputTitle VARCHAR(2000),
    IN inputAuthorName VARCHAR(1000),
    IN inputCategoryName  VARCHAR(2000),
    IN inputIsbn BIGINT,
    IN inputFormat VARCHAR(2000)
)
BEGIN
    SELECT b.title as "Title", a.author_name as "Author", c.category_name as "Category", f.format_name as "Format", b.isbn_13 as "ISBN", b.bestseller_rank as "Bestseller Ranking", b.rating_avg as "Average Rating"
    FROM book b
    LEFT JOIN author a ON b.author_id = a.author_id
    LEFT JOIN format f ON f.format_id = b.format_id
    LEFT JOIN category c ON b.category_id = c.category_id
    WHERE (inputTitle IS NULL OR TRIM(inputTitle) = '' OR b.title LIKE CONCAT('%', inputTitle, '%'))
      AND (inputAuthorName IS NULL OR TRIM(inputAuthorName) = '' OR a.author_name LIKE CONCAT('%', inputAuthorName, '%'))
      AND (inputCategoryName IS NULL OR TRIM(inputCategoryName) = '' OR c.category_name LIKE CONCAT('%', inputCategoryName, '%'))
      AND (inputFormat IS NULL OR TRIM(inputFormat) = '' OR f.format_name LIKE CONCAT('%', inputFormat, '%'))
      AND (inputIsbn IS NULL OR b.isbn_13 = inputIsbn);
    ORDER BY (b.bestseller_rank IS NULL), b.bestseller_rank ASC;
END $$

DELIMITER ;
