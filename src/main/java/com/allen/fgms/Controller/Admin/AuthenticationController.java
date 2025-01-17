package com.allen.fgms.Controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AuthenticationController {

    @FXML
    private TextField searchMembershipIDField;

    @FXML
    private TextField fullMembershipNameField;

    @FXML
    private TextField startDateField;

    @FXML
    private TextField endDateField;

    @FXML
    private TextField remainingDaysField;

    @FXML
    private TextField searchStaffIDField;

    @FXML
    private TextField fullStaffNameField;

    @FXML
    private TextField dateHiredField;

    @FXML
    private TextField positionField;

    @FXML
    private Button searchButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button clearStaffButton;

    @FXML
    private Button searchStaffButton;

    private int membershipId;


    public void initialize() {
        fullMembershipNameField.setEditable(false);
        startDateField.setEditable(false);
        endDateField.setEditable(false);
        remainingDaysField.setEditable(false);
        fullStaffNameField.setEditable(false);
        dateHiredField.setEditable(false);
        positionField.setEditable(false);

        searchButton.setOnMouseEntered(e -> searchButton.setStyle("-fx-background-color: #9145f5;"));
        searchButton.setOnMouseExited(e -> searchButton.setStyle("-fx-background-color:  #732bb5;"));

        clearButton.setOnMouseEntered(e -> clearButton.setStyle("-fx-background-color: #9145f5;"));
        clearButton.setOnMouseExited(e -> clearButton.setStyle("-fx-background-color:  #732bb5;"));

        searchStaffButton.setOnMouseEntered(e -> searchStaffButton.setStyle("-fx-background-color: #9145f5;"));
        searchStaffButton.setOnMouseExited(e -> searchStaffButton.setStyle("-fx-background-color:  #732bb5;"));

        clearStaffButton.setOnMouseEntered(e -> clearStaffButton.setStyle("-fx-background-color: #9145f5;"));
        clearStaffButton.setOnMouseExited(e -> clearStaffButton.setStyle("-fx-background-color:  #732bb5;"));
    }

    @FXML
    void onSearchMembershipButtonClicked() {
        String membershipIdStr = searchMembershipIDField.getText();

        if (membershipIdStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter Membership ID.");
            return;
        }

        try {
            membershipId = Integer.parseInt(membershipIdStr);

            Connection conn = com.allen.fgms.Model.DBConnection.getConnection();
            String query = "SELECT FullName, StartDate, EndDate FROM MembershipUsers WHERE MembershipID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, membershipId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String fullName = resultSet.getString("FullName");
                Date startDate = resultSet.getDate("StartDate");
                Date endDate = resultSet.getDate("EndDate");

                fullMembershipNameField.setText(fullName);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy  MMMM d", Locale.ENGLISH);

                if (startDate != null) {
                    LocalDate localStartDate = startDate.toLocalDate();
                    startDateField.setText(localStartDate.format(formatter));
                    startDateField.setPromptText("Start Date");
                } else {
                    startDateField.clear();
                    startDateField.setText("Cancelled Membership");
                }

                if (endDate != null) {
                    LocalDate localEndDate = endDate.toLocalDate();
                    endDateField.setText(localEndDate.format(formatter));
                    endDateField.setPromptText("End Date");

                    LocalDate currentDate = LocalDate.now();
                    long remainingDays = java.time.temporal.ChronoUnit.DAYS.between(currentDate, localEndDate);
                    if (remainingDays >= 0) {
                        remainingDaysField.setText(remainingDays + " days left");
                        remainingDaysField.setStyle("-fx-border-color: #00db37;");
                    } else {
                        remainingDaysField.setStyle("-fx-border-color: #ff0000;");
                        remainingDaysField.setText("Membership expired");
                    }
                } else {
                    endDateField.clear();
                    endDateField.setText("Cancelled Membership");
                    remainingDaysField.setStyle("-fx-border-color: #ff0000;");
                    remainingDaysField.setText("Cancelled Membership");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Membership ID not found.");
                clearMembershipFields();
            }

            conn.close();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching for the membership: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onSearchStaffButtonClicked(ActionEvent event) {
        String staffIdStr = searchStaffIDField.getText();

        if (staffIdStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter Staff ID.");
            return;
        }

        try {
            int staffId = Integer.parseInt(staffIdStr);

            Connection conn = com.allen.fgms.Model.DBConnection.getConnection();
            String query = "SELECT Name, DateHired, Position FROM users.Staff WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, staffId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String fullName = resultSet.getString("Name");
                Date dateHired = resultSet.getDate("DateHired");
                String position = resultSet.getString("Position");

                fullStaffNameField.setText(fullName);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM d", Locale.ENGLISH);

                    LocalDate localDateHired = dateHired.toLocalDate();
                    dateHiredField.setText(localDateHired.format(formatter));

                    positionField.setText(position);
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Staff ID not found.");
                clearStaffFields();
            }

            conn.close();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching for the staff: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onClearMembershipFieldClicked() {
        clearMembershipFields();
    }

    @FXML
    void onClearStaffFieldClicked() {
        clearStaffFields();
    }

    private void clearMembershipFields() {
        searchMembershipIDField.clear();
        fullMembershipNameField.clear();
        startDateField.clear();
        endDateField.clear();
        remainingDaysField.clear();
        remainingDaysField.setStyle("-fx-border-color: #732bb5;");
    }

    private void clearStaffFields() {
        searchStaffIDField.clear();
        fullStaffNameField.clear();
        dateHiredField.clear();
        positionField.clear();

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}