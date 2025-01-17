package com.allen.fgms.Controller.Admin;

import com.allen.fgms.Model.Membership;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class MembershipController {

    @FXML
    private TableView<Membership> membershipTable;
    @FXML
    private TableColumn<Membership, Integer> idColumn;
    @FXML
    private TableColumn<Membership, String> nameColumn;
    @FXML
    private TableColumn<Membership, String> startDateColumn;
    @FXML
    private TableColumn<Membership, String> endDateColumn;
    @FXML
    private TableColumn<Membership, String> contactColumn;
    @FXML
    private TableColumn<Membership, String> emailColumn;
    @FXML
    private TableColumn<Membership, Void> actionColumn;

    private ObservableList<Membership> membershipData = FXCollections.observableArrayList();

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button clearButton;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));

        loadMembershipData();
        membershipTable.setItems(membershipData);

        addButtonToTable();

        searchButton.setOnMouseEntered(e -> searchButton.setStyle("-fx-background-color: #9145f5;"));
        searchButton.setOnMouseExited(e -> searchButton.setStyle("-fx-background-color:  #732bb5;"));
        clearButton.setOnMouseEntered(e -> clearButton.setStyle("-fx-background-color: #9145f5;"));
        clearButton.setOnMouseExited(e -> clearButton.setStyle("-fx-background-color:  #732bb5;"));
    }

    public void loadMembershipData() {
        membershipData.clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy, MMMM d", Locale.ENGLISH);

        try {
            Connection conn =  com.allen.fgms.Model.DBConnection.getConnection();
            Statement statement = conn.createStatement();
            String query = "SELECT MembershipID, FullName, StartDate, EndDate, ContactNumber, EmailAddress FROM MembershipUsers";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("MembershipID");
                String fullName = resultSet.getString("FullName");

                Date startDate = resultSet.getDate("StartDate");
                String formattedStartDate = startDate != null ? startDate.toLocalDate().format(formatter) : "Cancelled\nMembership";

                Date endDate = resultSet.getDate("EndDate");
                String formattedEndDate = endDate != null ? endDate.toLocalDate().format(formatter) : "Cancelled\nMembership";

                String contactNumber = resultSet.getString("ContactNumber");
                String emailAddress = resultSet.getString("EmailAddress");

                Membership membership = new Membership(id, fullName, formattedStartDate, formattedEndDate, contactNumber, emailAddress);
                membershipData.add(membership);
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
            loadMembershipData();
        } else {
            membershipData.clear();
            try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
                String query = "SELECT MembershipID, FullName, StartDate, EndDate, ContactNumber, EmailAddress FROM MembershipUsers WHERE MembershipID = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(searchId));
                ResultSet resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("MembershipID");
                    String fullName = resultSet.getString("FullName");

                    Date startDate = resultSet.getDate("StartDate");
                    String formattedStartDate = startDate != null ? startDate.toLocalDate().format(formatter) : "Cancelled Membership";

                    Date endDate = resultSet.getDate("EndDate");
                    String formattedEndDate = endDate != null ? endDate.toLocalDate().format(formatter) : "Cancelled Membership";

                    String contactNumber = resultSet.getString("ContactNumber");
                    String emailAddress = resultSet.getString("EmailAddress");

                    Membership membership = new Membership(id, fullName, formattedStartDate, formattedEndDate, contactNumber, emailAddress);
                    membershipData.add(membership);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onClearButtonClicked(ActionEvent event) {
        searchField.clear();
        loadMembershipData();
    }

    private void addButtonToTable() {
        Callback<TableColumn<Membership, Void>, TableCell<Membership, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Membership, Void> call(final TableColumn<Membership, Void> param) {
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
                            Membership membership = getTableView().getItems().get(getIndex());
                            onEditButtonClicked(membership.getId());
                        });

                        deleteBtn.setOnAction((ActionEvent event) -> {
                            Membership membership = getTableView().getItems().get(getIndex());
                            onDeleteButtonClicked(membership.getId());
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


    private void onDeleteButtonClicked(int membershipId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this membership?");

        Optional<ButtonType> action = alert.showAndWait();
        if (action.isPresent() && action.get() == ButtonType.OK) {
            try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
                String query = "DELETE FROM MembershipUsers WHERE MembershipID = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, membershipId);
                stmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Membership deleted successfully.");
                loadMembershipData();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting membership: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void onEditButtonClicked(int membershipId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/EditMembership.fxml"));
            Parent root = loader.load();

            EditMembershipController editMembershipController = loader.getController();
            editMembershipController.setMembershipId(membershipId);
            editMembershipController.setMmController(this);

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
