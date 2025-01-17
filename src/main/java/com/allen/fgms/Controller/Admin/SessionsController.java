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

    private static final String ENTERED_BUTTON_STYLE = "-fx-background-color: #732bb5; -fx-text-fill: white;";
    private static final String EXITED_BUTTON_STYLE = "-fx-background-color: white; -fx-border-color: #732bb5;";

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

        allMonthButton.setOnMouseEntered(e -> allMonthButton.setStyle(ENTERED_BUTTON_STYLE));
        allMonthButton.setOnMouseExited(e -> allMonthButton.setStyle(EXITED_BUTTON_STYLE));
        januaryButton.setOnMouseEntered(e -> januaryButton.setStyle(ENTERED_BUTTON_STYLE));
        januaryButton.setOnMouseExited(e -> januaryButton.setStyle(EXITED_BUTTON_STYLE));
        februaryButton.setOnMouseEntered(e -> februaryButton.setStyle(ENTERED_BUTTON_STYLE));
        februaryButton.setOnMouseExited(e -> februaryButton.setStyle(EXITED_BUTTON_STYLE));
        marchButton.setOnMouseEntered(e -> marchButton.setStyle(ENTERED_BUTTON_STYLE));
        marchButton.setOnMouseExited(e -> marchButton.setStyle(EXITED_BUTTON_STYLE));
        aprilButton.setOnMouseEntered(e -> aprilButton.setStyle(ENTERED_BUTTON_STYLE));
        aprilButton.setOnMouseExited(e -> aprilButton.setStyle(EXITED_BUTTON_STYLE));
        mayButton.setOnMouseEntered(e -> mayButton.setStyle(ENTERED_BUTTON_STYLE));
        mayButton.setOnMouseExited(e -> mayButton.setStyle(EXITED_BUTTON_STYLE));
        juneButton.setOnMouseEntered(e -> juneButton.setStyle(ENTERED_BUTTON_STYLE));
        juneButton.setOnMouseExited(e -> juneButton.setStyle(EXITED_BUTTON_STYLE));
        julyButton.setOnMouseEntered(e -> julyButton.setStyle(ENTERED_BUTTON_STYLE));
        julyButton.setOnMouseExited(e -> julyButton.setStyle(EXITED_BUTTON_STYLE));
        augustButton.setOnMouseEntered(e -> augustButton.setStyle(ENTERED_BUTTON_STYLE));
        augustButton.setOnMouseExited(e -> augustButton.setStyle(EXITED_BUTTON_STYLE));
        septemberButton.setOnMouseEntered(e -> septemberButton.setStyle(ENTERED_BUTTON_STYLE));
        septemberButton.setOnMouseExited(e -> septemberButton.setStyle(EXITED_BUTTON_STYLE));
        octoberButton.setOnMouseEntered(e -> octoberButton.setStyle(ENTERED_BUTTON_STYLE));
        octoberButton.setOnMouseExited(e -> octoberButton.setStyle(EXITED_BUTTON_STYLE));
        novemberButton.setOnMouseEntered(e -> novemberButton.setStyle(ENTERED_BUTTON_STYLE));
        novemberButton.setOnMouseExited(e -> novemberButton.setStyle(EXITED_BUTTON_STYLE));
        decemberButton.setOnMouseEntered(e -> decemberButton.setStyle(ENTERED_BUTTON_STYLE));
        decemberButton.setOnMouseExited(e -> decemberButton.setStyle(EXITED_BUTTON_STYLE));

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
    }
}
