package com.allen.fgms.Controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    public void initialize() {
        usernameField.setOnKeyPressed(this::handleKeyPress);
        passwordField.setOnKeyPressed(this::handleKeyPress);

        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #9145f5;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color:  #732bb5;"));
    }

    @FXML
    void onLoginButtonClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all fields.");
            return;
        }

        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "SELECT * FROM users.Admin WHERE Username = ? AND Password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/MainWindow.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                Stage thisStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                thisStage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid username or password.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onLoginButtonClicked(new ActionEvent(loginButton, null));
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