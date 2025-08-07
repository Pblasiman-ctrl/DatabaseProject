CREATE DATABASE bookstore;
use bookstore;

CREATE TABLE author (
author_id BIGINT Primary Key,
author_name VARCHAR(2000));

CREATE TABLE category (
category_id INT Primary Key,
category_name VARCHAR(2000));

-- format was a key word in .sql swapped to "formatting"
CREATE TABLE formatting (
format_id BIGINT primary key,
format_name VARCHAR(2000));

CREATE TABLE book (
ibsn_13 BIGINT Primary Key,
bestseller_rank BIGINT,
description VARCHAR(5000),
format_id INT, -- FK to format, do we need to change the name of the column?
internal_id BIGINT,
language VARCHAR(20),
publication_date DATETIME,
average_rating FLOAT,
rating_count BIGINT,
weight DECIMAL(8,2),
title VARCHAR(2000));


CREATE TABLE reader (
user_id VARCHAR(200),
user_password VARCHAR(200),
primary key(user_id));

CREATE TABLE buys (
ibsn_13 BIGINT,
user_id VARCHAR(200),
FOREIGN KEY (ibsn_13) REFERENCES book(ibsn_13),
FOREIGN KEY (user_id) REFERENCES reader(user_id),
PRIMARY KEY (ibsn_13,user_id));

CREATE TABLE categoryOf (
ibsn_13 BIGINT,
category_id INT,
FOREIGN KEY (ibsn_13) REFERENCES book(ibsn_13),
FOREIGN KEY (category_id) REFERENCES category(category_id),
PRIMARY KEY (ibsn_13, category_id));
    
    
CREATE TABLE favoriteOf (
ibsn_13 BIGINT,
user_id VARCHAR(200),
FOREIGN KEY (ibsn_13) REFERENCES book(ibsn_13),
FOREIGN KEY (user_id) REFERENCES reader(user_id),
PRIMARY KEY (ibsn_13,user_id));

CREATE TABLE writes (
ibsn_13 BIGINT,
author_id BIGINT,
FOREIGN KEY (ibsn_13) REFERENCES book(ibsn_13),
FOREIGN KEY (author_id) REFERENCES author(author_id),
PRIMARY KEY (ibsn_13,author_id));

