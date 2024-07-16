CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    downloads INT DEFAULT 0,
    languages VARCHAR(10) NOT NULL,
    authors VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_year INT,
    death_year INT
);
