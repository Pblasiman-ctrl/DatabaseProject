DELIMITER $$

CREATE PROCEDURE TopFavoritedBooks(IN top_n INT)
BEGIN
    SELECT b.ibsn_13, b.title, COUNT(f.user_id) AS favorite_count
    FROM book b
    JOIN favoriteOf f ON b.ibsn_13 = f.ibsn_13
    GROUP BY b.ibsn_13, b.title
    ORDER BY favorite_count DESC
    LIMIT top_n;
END $$

DELIMITER ;