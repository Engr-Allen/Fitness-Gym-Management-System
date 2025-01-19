USE fgms;

ALTER TABLE users.Membership
ALTER COLUMN Start_Date DATE NULL;
ALTER TABLE users.Membership
ALTER COLUMN End_Date DATE NULL;