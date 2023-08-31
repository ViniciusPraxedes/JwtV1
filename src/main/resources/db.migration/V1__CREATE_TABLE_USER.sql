CREATE TABLE IF NOT EXISTS User (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      firstname VARCHAR(255),
                      lastname VARCHAR(255),
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      role VARCHAR(255) NOT NULL
);