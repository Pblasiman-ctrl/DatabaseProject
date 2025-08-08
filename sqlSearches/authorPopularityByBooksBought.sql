DELIMITER $$

CREATE PROCEDURE AuthorPopularityByBooksBought()
BEGIN
    SELECT a.author_id, a.author_name, COUNT(bu.user_id) AS books_bought
    FROM author a
    JOIN writes w ON a.author_id = w.author_id
    JOIN buys bu ON w.ibsn_13 = bu.ibsn_13
    GROUP BY a.author_id, a.author_name
    ORDER BY books_bought DESC;
END $$

DELIMITER ;