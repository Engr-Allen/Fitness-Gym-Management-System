USE fgms; -- Switch to the fgms database

-- Create the schema 'users' if it does not already exist
IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'users')
BEGIN
    EXEC('CREATE SCHEMA users');
END;

-- Create the 'Admin' table
CREATE TABLE users.Admin (
    AdminID INT IDENTITY(1,1) PRIMARY KEY, -- Unique identifier for each admin
    Username NVARCHAR(50) NOT NULL,        -- Admin username
    Password NVARCHAR(50) NOT NULL         -- Admin password
);
