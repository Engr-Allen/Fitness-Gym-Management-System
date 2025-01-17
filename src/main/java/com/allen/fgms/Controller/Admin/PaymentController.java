package com.allen.fgms.Controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class PaymentController {

    @FXML
    private ComboBox<String> membershipComboBox;

    @FXML
    private TextField newNameField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField emailAddressField;

    @FXML
    private TextField newReferenceField;

    @FXML
    private TextField searchMembershipID;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField startDateField;

    @FXML
    private TextField endDateField;

    @FXML
    private ComboBox<String> renewalPeriodComboBox;

    @FXML
    private ComboBox<String> paymentMethodComboBoxNew;

    @FXML
    private ComboBox<String> paymentMethodComboBoxRenew;

    @FXML
    private TextField renewReferenceField;

    @FXML
    private TextField newCashierField;

    @FXML
    private TextField renewCashierField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchButton;

    private int membershipId;
    private LocalDate currentEndDate;
    private LocalDate currentStartDate;

    @FXML
    public void initialize() {
        searchMembershipID.setEditable(true);

        membershipComboBox.getItems().addAll(
                "1 month (₱ 1000)",
                "3 months (₱ 2500)",
                "6 months (₱ 5000)",
                "1 year (₱ 9500)"
        );

        renewalPeriodComboBox.getItems().addAll(
                "1 month (₱ 1000)",
                "3 months (₱ 2500)",
                "6 months (₱ 5000)",
                "1 year (₱ 9500)"
        );

        paymentMethodComboBoxNew.getItems().addAll("In Person", "Gcash", "Paypal", "ATM Card");
        paymentMethodComboBoxRenew.getItems().addAll("In Person", "Gcash", "Paypal", "ATM Card");

        confirmButton.setOnMouseEntered(e -> confirmButton.setStyle("-fx-background-color: #9145f5;"));
        confirmButton.setOnMouseExited(e -> confirmButton.setStyle("-fx-background-color:  #732bb5;"));

        membershipComboBox.setOnMouseEntered(e -> membershipComboBox.setStyle("-fx-background-color: #9145f5;"));
        membershipComboBox.setOnMouseExited(e -> membershipComboBox.setStyle("-fx-border-color: #732bb5; -fx-background-color: white;"));

        paymentMethodComboBoxNew.setOnMouseEntered(e -> paymentMethodComboBoxNew.setStyle("-fx-background-color: #9145f5;"));
        paymentMethodComboBoxNew.setOnMouseExited(e -> paymentMethodComboBoxNew.setStyle("-fx-border-color: #732bb5; -fx-background-color: white;"));

        renewalPeriodComboBox.setOnMouseEntered(e -> renewalPeriodComboBox.setStyle("-fx-background-color: #9145f5;"));
        renewalPeriodComboBox.setOnMouseExited(e -> renewalPeriodComboBox.setStyle("-fx-border-color: #732bb5; -fx-background-color: white;"));

        paymentMethodComboBoxRenew.setOnMouseEntered(e -> paymentMethodComboBoxRenew.setStyle("-fx-background-color: #9145f5;"));
        paymentMethodComboBoxRenew.setOnMouseExited(e -> paymentMethodComboBoxRenew.setStyle("-fx-border-color: #732bb5; -fx-background-color: white;"));

        searchButton.setOnMouseEntered(e -> searchButton.setStyle("-fx-background-color: #9145f5;"));
        searchButton.setOnMouseExited(e -> searchButton.setStyle("-fx-background-color:  #732bb5;"));

        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #9145f5;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color:  #732bb5;"));
    }

    @FXML
    void onConfirmedButtonClicked() {
        String fullName = newNameField.getText();
        String membership = membershipComboBox.getValue();
        String contactNumber = contactField.getText();
        String emailAddress = emailAddressField.getText();
        String paymentMethod = paymentMethodComboBoxNew.getValue();
        String referenceNumber = newReferenceField.getText();
        String cashierName = newCashierField.getText();

        if (fullName.isEmpty() || membership == null || contactNumber.isEmpty() || emailAddress.isEmpty() || paymentMethod == null || cashierName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all fields.");
            return;
        }

        if (paymentMethod.equals("In Person") && (referenceNumber == null || referenceNumber.isEmpty())) {
            referenceNumber = generateReferenceNumber();
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = calculateEndDate(startDate, membership);
        double amount = calculateAmount(membership);

        int membershipId = 0;
        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String membershipQuery = "INSERT INTO MembershipUsers (FullName, StartDate, EndDate, ContactNumber, EmailAddress) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement membershipStmt = conn.prepareStatement(membershipQuery, Statement.RETURN_GENERATED_KEYS);
            membershipStmt.setString(1, fullName);
            membershipStmt.setDate(2, Date.valueOf(startDate));
            membershipStmt.setDate(3, Date.valueOf(endDate));
            membershipStmt.setString(4, contactNumber);
            membershipStmt.setString(5, emailAddress);
            membershipStmt.executeUpdate();

            ResultSet generatedKeys = membershipStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                membershipId = generatedKeys.getInt(1);
            }

            if (membershipId != 0) {
                String transactionQuery = "INSERT INTO UserTransactions (MembershipID, PaymentMethod, Amount, ReferenceNumber, TransactionType, CashierName) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement transactionStmt = conn.prepareStatement(transactionQuery);
                transactionStmt.setInt(1, membershipId);
                transactionStmt.setString(2, paymentMethod);
                transactionStmt.setDouble(3, amount);
                transactionStmt.setString(4, referenceNumber);
                transactionStmt.setString(5, "New Membership");
                transactionStmt.setString(6, cashierName);
                transactionStmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Membership added and transaction recorded successfully.");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/Receipt.fxml"));
                Parent receiptRoot = loader.load();

                ReceiptController receiptController = loader.getController();
                receiptController.setReceiptData(referenceNumber, startDate, endDate, fullName, membershipId, paymentMethod, amount, cashierName, "New Membership");

                Stage receiptStage = new Stage();
                receiptStage.setTitle("Transaction Receipt");
                receiptStage.setScene(new Scene(receiptRoot));
                receiptStage.show();

                receiptStage.setOnCloseRequest(event -> clearNewFields());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: MembershipID not generated.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onSaveButtonClicked() {
        String renewalPeriod = renewalPeriodComboBox.getValue();
        String paymentMethod = paymentMethodComboBoxRenew.getValue();
        String referenceNumber = renewReferenceField.getText();
        String cashierName = renewCashierField.getText();

        if (renewalPeriod == null || paymentMethod == null || fullNameField.getText().isEmpty() || cashierName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all fields.");
            return;
        }

        if (paymentMethod.equals("In Person") && (referenceNumber == null || referenceNumber.isEmpty())) {
            referenceNumber = generateReferenceNumber();
        }

        LocalDate newEndDate = currentEndDate != null && currentEndDate.isAfter(LocalDate.now())
                ? calculateNewEndDate(currentEndDate, renewalPeriod)
                : calculateNewEndDate(LocalDate.now(), renewalPeriod);

        double amount = calculateAmount(renewalPeriod);

        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String updateQuery = "UPDATE MembershipUsers SET EndDate = ?, StartDate = ? WHERE MembershipID = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setDate(1, Date.valueOf(newEndDate));
            updateStmt.setDate(2, Date.valueOf(LocalDate.now()));
            updateStmt.setInt(3, membershipId);
            updateStmt.executeUpdate();

            String transactionQuery = "INSERT INTO UserTransactions (MembershipID, PaymentMethod, Amount, ReferenceNumber, TransactionType, CashierName) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement transactionStmt = conn.prepareStatement(transactionQuery);
            transactionStmt.setInt(1, membershipId);
            transactionStmt.setString(2, paymentMethod);
            transactionStmt.setDouble(3, amount);
            transactionStmt.setString(4, referenceNumber);
            transactionStmt.setString(5, "Renew Membership");
            transactionStmt.setString(6, cashierName);
            transactionStmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Membership renewal and transaction recorded successfully.");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/Receipt.fxml"));
            Parent receiptRoot = loader.load();

            ReceiptController receiptController = loader.getController();
            receiptController.setReceiptData(referenceNumber, LocalDate.now(), newEndDate, fullNameField.getText(), membershipId, paymentMethod, amount, cashierName, "Renew Membership");

            Stage receiptStage = new Stage();
            receiptStage.setTitle("Transaction Receipt");
            receiptStage.setScene(new Scene(receiptRoot));
            receiptStage.show();

            receiptStage.setOnCloseRequest(event -> clearRenewFields());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating membership details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onSearchMembershipClicked() {
        String membershipIdStr = searchMembershipID.getText();

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

                fullNameField.setText(fullName);

                if (startDate != null) {
                    currentStartDate = startDate.toLocalDate();
                    startDateField.setText(currentStartDate.toString());
                    startDateField.setPromptText("Start Date");
                } else {
                    currentStartDate = null;
                    startDateField.clear();
                    startDateField.setPromptText("Cancelled Membership");
                }

                if (endDate != null) {
                    currentEndDate = endDate.toLocalDate();
                    endDateField.setText(currentEndDate.toString());
                    endDateField.setPromptText("End Date");
                } else {
                    currentEndDate = null;
                    endDateField.clear();
                    endDateField.setPromptText("Cancelled Membership");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Membership ID not found.");
                clearRenewFields();
            }

            conn.close();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching for the membership: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double calculateAmount(String membership) {
        switch (membership) {
            case "1 month (₱ 1000)":
                return 1000.0;
            case "3 months (₱ 2500)":
                return 2500.0;
            case "6 months (₱ 5000)":
                return 5000.0;
            case "1 year (₱ 9500)":
                return 9500.0;
            default:
                return 0.0;
        }
    }

    private LocalDate calculateEndDate(LocalDate startDate, String membership) {
        switch (membership) {
            case "1 month (₱ 1000)":
                return startDate.plusDays(30);
            case "3 months (₱ 2500)":
                return startDate.plusDays(90);
            case "6 months (₱ 5000)":
                return startDate.plusDays(180);
            case "1 year (₱ 9500)":
                return startDate.plusDays(365);
            default:
                return startDate;
        }
    }

    private LocalDate calculateNewEndDate(LocalDate startDate, String renewalPeriod) {
        switch (renewalPeriod) {
            case "1 month (₱ 1000)":
                return startDate.plusMonths(1);
            case "3 months (₱ 2500)":
                return startDate.plusMonths(3);
            case "6 months (₱ 5000)":
                return startDate.plusMonths(6);
            case "1 year (₱ 9500)":
                return startDate.plusYears(1);
            default:
                return startDate;
        }
    }

    private String generateReferenceNumber() {
        return String.format("%08d", (int) (Math.random() * 100_000_000));
    }

    private void clearNewFields() {
        newNameField.clear();
        membershipComboBox.setValue(null);
        contactField.clear();
        emailAddressField.clear();
        paymentMethodComboBoxNew.setValue(null);
        newReferenceField.clear();
        newCashierField.clear();
    }

    private void clearRenewFields() {
        searchMembershipID.clear();
        fullNameField.clear();
        startDateField.clear();
        endDateField.clear();
        renewalPeriodComboBox.setValue(null);
        paymentMethodComboBoxRenew.setValue(null);
        renewReferenceField.clear();
        renewCashierField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}