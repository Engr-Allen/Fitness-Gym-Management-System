package com.allen.fgms.Controller.Admin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MainWindowController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Button membershipButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button authenticationButton;
    @FXML
    private Button msButton;
    @FXML
    private Button sessionsButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button paymentButton;
    @FXML
    private Button transactionsButton;

    @FXML
    private FontAwesomeIcon dashboardIcon;
    @FXML
    private FontAwesomeIcon authenticationIcon;
    @FXML
    private FontAwesomeIcon paymentIcon;
    @FXML
    private FontAwesomeIcon membershipIcon;
    @FXML
    private FontAwesomeIcon msIcon;
    @FXML
    private FontAwesomeIcon sessionsIcon;
    @FXML
    private FontAwesomeIcon transactionsIcon;
    @FXML
    private FontAwesomeIcon logoutIcon;

    private static final String DEFAULT_BUTTON_STYLE = "-fx-background-color: white; -fx-text-fill: black;";
    private static final String ACTIVE_BUTTON_STYLE = "-fx-background-color: #732bb5; -fx-text-fill: white;";

    @FXML
    public void initialize() {
        onDashboardButtonClicked(new ActionEvent());
    }

    @FXML
    void onAuthenticationButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/Authentication.fxml"));
            Parent authenticationContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(authenticationContent);

            resetButtonStyles();
            authenticationButton.setStyle(ACTIVE_BUTTON_STYLE);
            authenticationIcon.getStyleClass().clear();
            authenticationIcon.getStyleClass().add("icon-white");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onLogoutButtonClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Are you sure you want to logout?");

        logoutButton.setStyle(ACTIVE_BUTTON_STYLE);
        logoutIcon.getStyleClass().clear();
        logoutIcon.getStyleClass().add("icon-white");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                Stage thisStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                thisStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logoutButton.setStyle(DEFAULT_BUTTON_STYLE);
            logoutIcon.getStyleClass().clear();
            logoutIcon.getStyleClass().add("icon-default");
        }
    }

    @FXML
    void onMembershipButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/Membership.fxml"));
            Parent mmContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(mmContent);

            resetButtonStyles();
            membershipButton.setStyle(ACTIVE_BUTTON_STYLE);
            membershipIcon.getStyleClass().clear();
            membershipIcon.getStyleClass().add("icon-white");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onMSButtonClicked(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/MS.fxml"));
            Parent msContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(msContent);

            resetButtonStyles();
            msButton.setStyle(ACTIVE_BUTTON_STYLE);
            msIcon.getStyleClass().clear();
            msIcon.getStyleClass().add("icon-white");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSessionsButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/Sessions.fxml"));
            Parent sessionsContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(sessionsContent);

            resetButtonStyles();
            sessionsButton.setStyle(ACTIVE_BUTTON_STYLE);
            sessionsIcon.getStyleClass().clear();
            sessionsIcon.getStyleClass().add("icon-white");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onDashboardButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/Dashboard.fxml"));
            Parent dashboardContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(dashboardContent);

            resetButtonStyles();
            dashboardButton.setStyle(ACTIVE_BUTTON_STYLE);
            dashboardIcon.getStyleClass().clear();
            dashboardIcon.getStyleClass().add("icon-white");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onPaymentButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/Payment.fxml"));
            Parent transactionContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(transactionContent);

            resetButtonStyles();
            paymentButton.setStyle(ACTIVE_BUTTON_STYLE);
            paymentIcon.getStyleClass().clear();
            paymentIcon.getStyleClass().add("icon-white");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onTransactionsButtonClicked(ActionEvent event) {

        resetButtonStyles();
        transactionsButton.setStyle(ACTIVE_BUTTON_STYLE);
        transactionsIcon.getStyleClass().clear();
        transactionsIcon.getStyleClass().add("icon-white");
    }

    private void resetButtonStyles() {
        membershipButton.setStyle(DEFAULT_BUTTON_STYLE);
        authenticationButton.setStyle(DEFAULT_BUTTON_STYLE);
        msButton.setStyle(DEFAULT_BUTTON_STYLE);
        sessionsButton.setStyle(DEFAULT_BUTTON_STYLE);
        logoutButton.setStyle(DEFAULT_BUTTON_STYLE);
        dashboardButton.setStyle(DEFAULT_BUTTON_STYLE);
        paymentButton.setStyle(DEFAULT_BUTTON_STYLE);
        transactionsButton.setStyle(DEFAULT_BUTTON_STYLE);
        logoutButton.setStyle(DEFAULT_BUTTON_STYLE);

        dashboardIcon.getStyleClass().clear();
        dashboardIcon.getStyleClass().add("icon-default");
        authenticationIcon.getStyleClass().clear();
        authenticationIcon.getStyleClass().add("icon-default");
        paymentIcon.getStyleClass().clear();
        paymentIcon.getStyleClass().add("icon-default");
        membershipIcon.getStyleClass().clear();
        membershipIcon.getStyleClass().add("icon-default");
        msIcon.getStyleClass().clear();
        msIcon.getStyleClass().add("icon-default");
        sessionsIcon.getStyleClass().clear();
        sessionsIcon.getStyleClass().add("icon-default");
        transactionsIcon.getStyleClass().clear();
        transactionsIcon.getStyleClass().add("icon-default");
        logoutIcon.getStyleClass().clear();
        logoutIcon.getStyleClass().add("icon-default");
    }
}