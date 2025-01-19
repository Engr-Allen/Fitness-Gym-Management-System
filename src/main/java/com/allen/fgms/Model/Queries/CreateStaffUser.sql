USE fgms;

CREATE TABLE users.Staff (
    ID INT NOT NULL IDENTITY(1300,1) PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    DateHired DATE NOT NULL,
    ContactNumber VARCHAR(15),
	EmailAddress VARCHAR(255),
	Position VARCHAR(255)
);