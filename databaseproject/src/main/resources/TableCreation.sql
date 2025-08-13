use bookstore;

CREATE TABLE author (
author_id BIGINT Primary Key,
author_name VARCHAR(2000));

CREATE TABLE category (
category_id INT Primary Key,
category_name VARCHAR(2000));

-- format was a key word in .sql swapped to "formatting"
CREATE TABLE format (
format_id BIGINT primary key,
format_name VARCHAR(2000));

CREATE TABLE book (
author_id BIGINT,
bestseller_rank BIGINT,
category_id INT,
description VARCHAR(5000),
format_id INT,
isbn_13 BIGINT Primary Key,
publication_date DATETIME,
average_rating FLOAT,
rating_count BIGINT,
title VARCHAR(2000),
weight DECIMAL(8,2));


CREATE TABLE reader (
user_id VARCHAR(200),
user_password VARCHAR(200),
primary key(user_id));

CREATE TABLE buys (
isbn_13 BIGINT,
user_id VARCHAR(200),
FOREIGN KEY (isbn_13) REFERENCES book(isbn_13),
FOREIGN KEY (user_id) REFERENCES reader(user_id),
PRIMARY KEY (isbn_13,user_id));

CREATE TABLE categoryOf (
isbn_13 BIGINT,
category_id INT,
FOREIGN KEY (isbn_13) REFERENCES book(isbn_13),
FOREIGN KEY (category_id) REFERENCES category(category_id),
PRIMARY KEY (isbn_13, category_id));  
    
CREATE TABLE favoriteOf (
isbn_13 BIGINT,
user_id VARCHAR(200),
FOREIGN KEY (isbn_13) REFERENCES book(isbn_13),
FOREIGN KEY (user_id) REFERENCES reader(user_id),
PRIMARY KEY (isbn_13,user_id));

