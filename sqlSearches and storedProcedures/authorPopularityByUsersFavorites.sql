DELIMITER $$

CREATE PROCEDURE AuthorPopularityByUsersFavorites()
BEGIN
    SELECT a.author_id, a.author_name, COUNT(f.user_id) AS books_favorited
    FROM author a
    JOIN writes w ON a.author_id = w.author_id
    JOIN favoriteOf f ON w.ibsn_13 = f.ibsn_13
    GROUP BY a.author_id, a.author_name
    ORDER BY books_favorited DESC;
END $$

DELIMITER ;