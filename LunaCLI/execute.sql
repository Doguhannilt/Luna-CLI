
DROP TABLE IF EXISTS example;


CREATE TABLE example (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);


INSERT INTO example (username, email, password) VALUES
('john_doe', 'john@example.com', 'hashed_password_123'),
('jane_doe', 'jane@example.com', 'hashed_password_456'),
('alice_smith', 'alice@example.com', 'hashed_password_789');


SELECT * FROM example;
