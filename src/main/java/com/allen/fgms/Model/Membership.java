package com.allen.fgms.Model;

public class Membership {
    private int id;
    private String fullName;
    private String startDate;
    private String endDate;
    private String contactNumber;
    private String emailAddress;

    public Membership(int id, String fullName, String startDate, String endDate, String contactNumber, String emailAddress) {
        this.id = id;
        this.fullName = fullName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}