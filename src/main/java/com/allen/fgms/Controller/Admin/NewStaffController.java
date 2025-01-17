package com.allen.fgms.Controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.time.LocalDate;

public class NewStaffController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField contactNumberField;

    @FXML
    private TextField emailAddressField;

    @FXML
    private TextField positionField;

    private MSController msController;

    public void setMsController(MSController msController) {
        this.msController = msController;
    }

    @FXML
    void onConfirmedButtonClicked() {
        String name = nameField.getText();
        String contactNumber = contactNumberField.getText();
        String emailAddress = emailAddressField.getText();
        String position = positionField.getText();

        if (name.isEmpty() || contactNumber.isEmpty() || emailAddress.isEmpty() || position.isEmpty()) {
            showAlert(AlertType.WARNING, "Warning", "Please fill in all fields.");
            return;
        }

        LocalDate dateHired = LocalDate.now();

        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "INSERT INTO users.Staff (Name, DateHired, ContactNumber, EmailAddress, Position) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setDate(2, Date.valueOf(dateHired));
            stmt.setString(3, contactNumber);
            stmt.setString(4, emailAddress);
            stmt.setString(5, position);

            stmt.executeUpdate();
            showAlert(AlertType.INFORMATION, "Success", "Staff added successfully.");

            if (msController != null) {
                msController.loadStaffData();
            } clearFields();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        contactNumberField.clear();
        emailAddressField.clear();
        positionField.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}