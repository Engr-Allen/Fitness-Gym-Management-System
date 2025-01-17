package com.allen.fgms.Controller.Admin;

import com.allen.fgms.Model.Staff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import javafx.util.Callback;

public class MSController {

    @FXML
    private TableView<Staff> staffTable;
    @FXML
    private TableColumn<Staff, Integer> idColumn;
    @FXML
    private TableColumn<Staff, String> nameColumn;
    @FXML
    private TableColumn<Staff, String> dateHiredColumn;
    @FXML
    private TableColumn<Staff, String> contactNumberColumn;
    @FXML
    private TableColumn<Staff, String> emailAddressColumn;
    @FXML
    private TableColumn<Staff, String> positionColumn;
    @FXML
    private TableColumn<Staff, Void> actionColumn;

    private ObservableList<Staff> staffData = FXCollections.observableArrayList();

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button newStaffButton;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateHiredColumn.setCellValueFactory(new PropertyValueFactory<>("dateHired"));
        contactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        emailAddressColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        loadStaffData();
        staffTable.setItems(staffData);

        addButtonToTable();

        searchButton.setOnMouseEntered(e -> searchButton.setStyle("-fx-background-color: #9145f5;"));
        searchButton.setOnMouseExited(e -> searchButton.setStyle("-fx-background-color:  #732bb5;"));
        clearButton.setOnMouseEntered(e -> clearButton.setStyle("-fx-background-color: #9145f5;"));
        clearButton.setOnMouseExited(e -> clearButton.setStyle("-fx-background-color: #732bb5;"));
        newStaffButton.setOnMouseEntered(e -> newStaffButton.setStyle("-fx-background-color: #732bb5; -fx-text-fill: white;"));
        newStaffButton.setOnMouseExited(e -> newStaffButton.setStyle("-fx-background-color: white; -fx-border-color: #732bb5;"));
    }

    public void loadStaffData() {
        staffData.clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy, MMMM d", Locale.ENGLISH);

        try {
            Connection conn =  com.allen.fgms.Model.DBConnection.getConnection();
            Statement statement = conn.createStatement();
            String query = "SELECT ID, Name, DateHired, ContactNumber, EmailAddress, Position FROM users.Staff";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");

                Date dateHired = resultSet.getDate("DateHired");
                String formattedDateHired = dateHired != null ? dateHired.toLocalDate().format(formatter) : "No Hiring Date";

                String contactNumber = resultSet.getString("ContactNumber");
                String emailAddress = resultSet.getString("EmailAddress");
                String position = resultSet.getString("Position");

                Staff staff = new Staff(id, name, formattedDateHired, contactNumber, emailAddress, position);
                staffData.add(staff);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSearchButtonClicked() {
        String searchId = searchField.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM d", Locale.ENGLISH);

        if (searchId == null || searchId.isEmpty()) {
            loadStaffData();
        } else {
            staffData.clear();
            try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
                String query = "SELECT ID, Name, DateHired, ContactNumber, EmailAddress, Position FROM users.Staff WHERE ID = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(searchId));
                ResultSet resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");

                    Date dateHired = resultSet.getDate("DateHired");
                    String formattedDateHired = dateHired != null ? dateHired.toLocalDate().format(formatter) : "No Hiring Date";

                    String contactNumber = resultSet.getString("ContactNumber");
                    String emailAddress = resultSet.getString("EmailAddress");
                    String position = resultSet.getString("Position");

                    Staff staff = new Staff(id, name, formattedDateHired, contactNumber, emailAddress, position);
                    staffData.add(staff);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onClearButtonClicked(ActionEvent event) {
        searchField.clear();
        loadStaffData();
    }

    private void addButtonToTable() {
        Callback<TableColumn<Staff, Void>, TableCell<Staff, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Staff, Void> call(final TableColumn<Staff, Void> param) {
                return new TableCell<>() {

                    private final Button editBtn = new Button("Edit");
                    private final Button deleteBtn = new Button("Delete");

                    {
                        editBtn.setStyle("-fx-background-color: #732bb5; -fx-text-fill: white; -fx-pref-width: 70; -fx-pref-height: 35; -fx-background-radius: 15; -fx-border-radius: 15;");
                        editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color: #9145f5; -fx-text-fill: white; -fx-pref-width: 70; -fx-pref-height: 35; -fx-background-radius: 15; -fx-border-radius: 15;"));
                        editBtn.setOnMouseExited(e -> editBtn.setStyle("-fx-background-color:  #732bb5; -fx-text-fill: white; -fx-pref-width: 70; -fx-pref-height: 35; -fx-background-radius: 15; -fx-border-radius: 15;"));

                        deleteBtn.setStyle("-fx-background-color: #b02a24; -fx-text-fill: white; -fx-pref-width: 70; -fx-pref-height: 35; -fx-background-radius: 15; -fx-border-radius: 15;");
                        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #e19238; -fx-text-fill: white; -fx-pref-width: 70; -fx-pref-height: 35; -fx-background-radius: 15; -fx-border-radius: 15;"));
                        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle("-fx-background-color:  #b02a24; -fx-text-fill: white; -fx-pref-width: 70; -fx-pref-height: 35; -fx-background-radius: 15; -fx-border-radius: 15;"));

                        editBtn.setOnAction((ActionEvent event) -> {
                            Staff staff = getTableView().getItems().get(getIndex());
                            onEditButtonClicked(staff.getId());
                        });

                        deleteBtn.setOnAction((ActionEvent event) -> {
                            Staff staff = getTableView().getItems().get(getIndex());
                            onDeleteButtonClicked(staff.getId());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            FlowPane pane = new FlowPane(editBtn, deleteBtn);
                            pane.setHgap(10);
                            pane.setAlignment(Pos.CENTER);
                            setGraphic(pane);
                        }
                    }
                };
            }
        };
        actionColumn.setCellFactory(cellFactory);
    }

    private void onDeleteButtonClicked(int staffId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this staff?");

        Optional<ButtonType> action = alert.showAndWait();
        if (action.isPresent() && action.get() == ButtonType.OK) {
            try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
                String query = "DELETE FROM users.Staff WHERE ID = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, staffId);
                stmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Staff deleted successfully.");
                loadStaffData();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting staff: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void onEditButtonClicked(int staffId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/EditStaff.fxml"));
            Parent root = loader.load();

            EditStaffController editStaffController = loader.getController();
            editStaffController.setStaffId(staffId);
            editStaffController.setMsController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onNSButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/NewStaff.fxml"));
            Parent root = loader.load();

            NewStaffController newStaffController = loader.getController();
            newStaffController.setMsController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
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