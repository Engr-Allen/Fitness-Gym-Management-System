    package com.allen.fgms.Controller.Admin;

    import javafx.fxml.FXML;
    import javafx.scene.control.Label;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;

    public class ReceiptController {

        @FXML
        private Label referenceLabel;
        @FXML
        private Label dateTimeLabel;
        @FXML
        private Label transactionTypeLabel;
        @FXML
        private Label nameLabel;
        @FXML
        private Label membershipIDLabel;
        @FXML
        private Label paymentMethodLabel;
        @FXML
        private Label amountLabel;
        @FXML
        private Label cashierLabel;
        @FXML
        private Label startDateLabel;
        @FXML
        private Label endDateLabel;

        private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy MMMM d");
        private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MMMM d, h:mm a");

        public void setReceiptData(String reference, LocalDate startDate, LocalDate endDate, String name,
                                   int membershipID, String paymentMethod, double amount, String cashier, String transactionType) {
            referenceLabel.setText(reference);
            dateTimeLabel.setText(LocalDateTime.now().format(dateTimeFormatter));
            transactionTypeLabel.setText(transactionType);
            nameLabel.setText(name);
            membershipIDLabel.setText(String.valueOf(membershipID));
            paymentMethodLabel.setText(paymentMethod);
            amountLabel.setText("₱ " + amount);
            cashierLabel.setText(cashier);
            startDateLabel.setText(startDate.format(dateFormatter));
            endDateLabel.setText(endDate.format(dateFormatter));
        }
    }
