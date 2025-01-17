USE fgms;

CREATE TABLE MembershipUsers(
	MembershipID INT NOT NULL PRIMARY KEY IDENTITY(543000, 1),
	FullName VARCHAR(255) NOT NULL,
    StartDate DATE NULL,
    EndDate DATE NULL,
    ContactNumber VARCHAR(255),
	EmailAddress VARCHAR(255)		
);