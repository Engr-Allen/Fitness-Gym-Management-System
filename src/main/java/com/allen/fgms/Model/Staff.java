package com.allen.fgms.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Staff {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty dateHired;
    private final SimpleStringProperty emailAddress;
    private final SimpleStringProperty contactNumber;
    private final SimpleStringProperty position;

    public Staff(int id, String name, String dateHired, String contactNumber, String emailAddress, String position) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.dateHired = new SimpleStringProperty(dateHired);
        this.contactNumber = new SimpleStringProperty(contactNumber);
        this.emailAddress = new SimpleStringProperty(emailAddress);
        this.position = new SimpleStringProperty(position);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDateHired() {
        return dateHired.get();
    }

    public void setDateHired(String dateHired) {
        this.dateHired.set(dateHired);
    }

    public String getContactNumber() {
        return contactNumber.get();
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber.set(contactNumber);
    }

    public String getEmailAddress() {
        return emailAddress.get();
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress.set(emailAddress);
    }

    public String getPosition() {
        return position.get();
    }

    public void setPosition(String position) {
        this.position.set(position);
    }
}
