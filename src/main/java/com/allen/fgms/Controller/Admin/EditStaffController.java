package com.allen.fgms.Controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;

public class EditStaffController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField contactNumberField;
    @FXML
    private TextField emailAddressField;
    @FXML
    private TextField positionField;

    @FXML
    private Button saveButton;

    private MSController msController;

    public void setMsController(MSController msController) {
        this.msController = msController;
    }
    private int staffId;
    private LocalDate currentdateHired;

    public void setStaffId(int staffId) {
        this.staffId = staffId;
        loadStaffDetails();
    }

    @FXML
    public void initialize() {
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #9145f5;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color:  #732bb5;"));
    }

    private void loadStaffDetails() {
        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "SELECT Name, DateHired, ContactNumber, EmailAddress, Position FROM users.Staff WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, staffId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("Name"));
                contactNumberField.setText(rs.getString("ContactNumber"));
                emailAddressField.setText(rs.getString("EmailAddress"));
                positionField.setText(rs.getString("Position"));

                Date dateHired = rs.getDate("DateHired");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading staff details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onSaveButtonClicked() {
        String name = nameField.getText();
        String contactNumber = contactNumberField.getText();
        String emailAddress = emailAddressField.getText();
        String position = positionField.getText();

        if (name.isEmpty() || contactNumber.isEmpty() || emailAddress.isEmpty() || position.isEmpty()) {
            showAlert(AlertType.WARNING, "Warning", "Please fill in all fields.");
            return;
        }

        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "UPDATE users.Staff SET Name = ?, ContactNumber = ?, EmailAddress = ?, Position = ? WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, contactNumber);
            stmt.setString(3, emailAddress);
            stmt.setString(4, position);
            stmt.setInt(5, staffId);

            stmt.executeUpdate();
            showAlert(AlertType.INFORMATION, "Success", "Staff details updated successfully.");

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

            if (msController != null) {
                msController.loadStaffData();
            }
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "An error occurred while updating membership details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}