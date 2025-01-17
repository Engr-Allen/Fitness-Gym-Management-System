package com.allen.fgms.Controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EditMembershipController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField emailField;

    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    private MembershipController membershipController;

    public void setMmController(MembershipController membershipController) { this.membershipController = membershipController; }
    private int membershipId;

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
        loadMembershipDetails();

        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle("-fx-background-color: #9145f5;"));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle("-fx-background-color:  #732bb5;"));

        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #9145f5;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color:  #732bb5;"));
    }

    private void loadMembershipDetails() {
        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "SELECT FullName, ContactNumber, EmailAddress FROM MembershipUsers WHERE MembershipID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, membershipId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("FullName"));
                contactField.setText(rs.getString("ContactNumber"));
                emailField.setText(rs.getString("EmailAddress"));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading membership details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onSaveButtonClicked() {
        String name = nameField.getText();
        String contact = contactField.getText();
        String email = emailField.getText();

        if (name.isEmpty() || contact.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all fields.");
            return;
        }

        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "UPDATE MembershipUsers SET FullName = ?, ContactNumber = ?, EmailAddress = ? WHERE MembershipID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, contact);
            stmt.setString(3, email);
            stmt.setInt(4, membershipId);

            stmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Membership details updated successfully.");

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

            if (membershipController != null) {
                membershipController.loadMembershipData();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating membership details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onCancelButtonClicked() {
        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "UPDATE MembershipUsers SET StartDate = NULL, EndDate = NULL WHERE MembershipID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, membershipId);

            stmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Membership cancelled successfully.");

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

            if (membershipController != null) {
                membershipController.loadMembershipData();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while cancelling membership: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
