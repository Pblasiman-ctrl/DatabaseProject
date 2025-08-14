DELIMITER $$

CREATE PROCEDURE FindBooksBoughtByUser(IN inputUserID VARCHAR(200))
BEGIN
    SELECT b.title
            ,a.author_name
            ,cat.category_name
            ,form.format_name
            ,b.description
            ,b.isbn_13
            ,b.average_rating
    FROM book b
    JOIN buys bu ON b.isbn_13 = bu.isbn_13
    JOIN author a ON b.author_id = a.author_id
    JOIN category cat ON b.category_id = cat.category_id
    JOIN format form ON form.format_id = b.format_id
    WHERE bu.user_id = inputUserID;
END $$

DELIMITER ;
