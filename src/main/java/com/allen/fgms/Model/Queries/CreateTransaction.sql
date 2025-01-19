USE fgms;

CREATE TABLE UserTransactions (
    TransactionID INT NOT NULL PRIMARY KEY IDENTITY(1, 1),
    MembershipID INT NOT NULL,
    PaymentMethod VARCHAR(50) NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    DateTime DATETIME NOT NULL DEFAULT GETDATE(),
    ReferenceNumber VARCHAR(20),
    FOREIGN KEY (MembershipID) REFERENCES MembershipUsers(MembershipID),
	TransactionType VARCHAR(20) NOT NULL
);
