package com.allen.fgms.Controller.Admin;

import com.allen.fgms.Model.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SessionsController {

    @FXML
    private TableView<Session> sessionsTable;
    @FXML
    private TableColumn<Session, String> dateColumn;
    @FXML
    private TableColumn<Session, String> startTimeColumn;
    @FXML
    private TableColumn<Session, String> endTimeColumn;
    @FXML
    private TableColumn<Session, String> sessionColumn;
    @FXML
    private TableColumn<Session, String> facilitatorsColumn;
    @FXML
    private TableColumn<Session, String> locationColumn;

    private ObservableList<Session> sessionList = FXCollections.observableArrayList();

    @FXML
    private Button allMonthButton;
    @FXML
    private Button januaryButton;
    @FXML
    private Button februaryButton;
    @FXML
    private Button marchButton;
    @FXML
    private Button aprilButton;
    @FXML
    private Button mayButton;
    @FXML
    private Button juneButton;
    @FXML
    private Button julyButton;
    @FXML
    private Button augustButton;
    @FXML
    private Button septemberButton;
    @FXML
    private Button octoberButton;
    @FXML
    private Button novemberButton;
    @FXML
    private Button decemberButton;
    @FXML
    private Button csButton;

    private static final String ACTIVE_BUTTON_STYLE = "-fx-background-color: #732bb5; -fx-text-fill: white;";
    private static final String DEFAULT_BUTTON_STYLE = "-fx-background-color: white; -fx-border-color: #732bb5;";

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        sessionColumn.setCellValueFactory(new PropertyValueFactory<>("session"));
        facilitatorsColumn.setCellValueFactory(new PropertyValueFactory<>("facilitator"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        loadSessionsFromDatabase();
        displayCurrentMonthSessions();

        allMonthButton.setOnAction(this::handleAllMonthButtonAction);
        januaryButton.setOnAction(this::handleMonthButtonAction);
        februaryButton.setOnAction(this::handleMonthButtonAction);
        marchButton.setOnAction(this::handleMonthButtonAction);
        aprilButton.setOnAction(this::handleMonthButtonAction);
        mayButton.setOnAction(this::handleMonthButtonAction);
        juneButton.setOnAction(this::handleMonthButtonAction);
        julyButton.setOnAction(this::handleMonthButtonAction);
        augustButton.setOnAction(this::handleMonthButtonAction);
        septemberButton.setOnAction(this::handleMonthButtonAction);
        octoberButton.setOnAction(this::handleMonthButtonAction);
        novemberButton.setOnAction(this::handleMonthButtonAction);
        decemberButton.setOnAction(this::handleMonthButtonAction);

        setCurrentMonthButtonStyle();

        csButton.setOnMouseEntered(e -> csButton.setStyle("-fx-background-color: #9145f5;"));
        csButton.setOnMouseExited(e -> csButton.setStyle("-fx-background-color:  #732bb5;"));
    }

    public void loadSessionsFromDatabase() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.Sessions")) {

            while (rs.next()) {
                Date date = rs.getDate("Date");
                String formattedDate = date != null ? date.toLocalDate().format(formatter) : "";

                String startTime = rs.getString("StartTime");
                String endTime = rs.getString("EndTime");
                String session = rs.getString("Session");
                String facilitator = rs.getString("Facilitators");
                String location = rs.getString("Location");

                sessionList.add(new Session(formattedDate, startTime, endTime, session, facilitator, location));
            }

            sessionsTable.setItems(sessionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onCSButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/CS.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayCurrentMonthSessions() {
        LocalDate now = LocalDate.now();
        String currentMonth = now.getMonth().name();

        ObservableList<Session> filteredList = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);

        for (Session session : sessionList) {
            LocalDate date = LocalDate.parse(session.getDate(), formatter);
            if (date.getMonth().name().equalsIgnoreCase(currentMonth)) {
                filteredList.add(session);
            }
        }

        sessionsTable.setItems(filteredList);
    }

    private void handleAllMonthButtonAction(ActionEvent event) {
        sessionsTable.setItems(sessionList);

        resetButtonStyles();

        allMonthButton.setStyle(ACTIVE_BUTTON_STYLE);
    }

    private void handleMonthButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String month = clickedButton.getText();

        ObservableList<Session> filteredList = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);

        for (Session session : sessionList) {
            LocalDate date = LocalDate.parse(session.getDate(), formatter);
            if (date.getMonth().name().equalsIgnoreCase(month)) {
                filteredList.add(session);
            }
        }

        sessionsTable.setItems(filteredList);

        resetButtonStyles();

        clickedButton.setStyle(ACTIVE_BUTTON_STYLE);
    }

    private void resetButtonStyles() {
        allMonthButton.setStyle(DEFAULT_BUTTON_STYLE);
        januaryButton.setStyle(DEFAULT_BUTTON_STYLE);
        februaryButton.setStyle(DEFAULT_BUTTON_STYLE);
        marchButton.setStyle(DEFAULT_BUTTON_STYLE);
        aprilButton.setStyle(DEFAULT_BUTTON_STYLE);
        mayButton.setStyle(DEFAULT_BUTTON_STYLE);
        juneButton.setStyle(DEFAULT_BUTTON_STYLE);
        julyButton.setStyle(DEFAULT_BUTTON_STYLE);
        augustButton.setStyle(DEFAULT_BUTTON_STYLE);
        septemberButton.setStyle(DEFAULT_BUTTON_STYLE);
        octoberButton.setStyle(DEFAULT_BUTTON_STYLE);
        novemberButton.setStyle(DEFAULT_BUTTON_STYLE);
        decemberButton.setStyle(DEFAULT_BUTTON_STYLE);
    }

    private void setCurrentMonthButtonStyle() {
        LocalDate now = LocalDate.now();
        String currentMonth = now.getMonth().name();

        switch (currentMonth.toLowerCase()) {
            case "january":
                januaryButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "february":
                februaryButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "march":
                marchButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "april":
                aprilButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "may":
                mayButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "june":
                juneButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "july":
                julyButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "august":
                augustButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "september":
                septemberButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "october":
                octoberButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "november":
                novemberButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
            case "december":
                decemberButton.setStyle(ACTIVE_BUTTON_STYLE);
                break;
        }
    }
}
