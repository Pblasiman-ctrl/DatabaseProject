CREATE DATABASE bookstore;
use bookstore;

CREATE TABLE author (
author_id BIGINT,
author_name VARCHAR(2000),
primary key(author_id));

CREATE TABLE category (
category_id BIGINT,
category_name VARCHAR(2000),
primary key(category_id));

CREATE TABLE format (
format_id BIGINT,
format_name VARCHAR(2000),
primary key(format_id));

CREATE TABLE book (
ibsn_13 BIGINT,
bestseller_rank BIGINT,
description VARCHAR(5000),
format BIGINT,
internal_id BIGINT,
illustrations VARCHAR(2000),
imprint VARCHAR(2000),
language VARCHAR(20),
publication_date DATETIME,
average_rating FLOAT,
rating_count BIGINT,
title VARCHAR(2000),
primary key(ibsn_13));

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
category_id BIGINT,
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

