USE fgms;

CREATE TABLE Sessions(
	ID INT NOT NULL IDENTITY(1, 1) PRIMARY KEY,
	Session VARCHAR(255),
	Date DATE NOT NULL,
	StartTime time NOT NULL,
	EndTime time NOT NULL,
	Facilitators VARCHAR(255),
	Location VARCHAR(255)
);