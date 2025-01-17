package com.allen.fgms.Controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardController {

    @FXML
    private PieChart membershipPieChart;

    @FXML
    private BarChart<String, Number> membershipBarChart;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private Label totalEarningsLabel;

    private Map<String, Color> colorMap = new HashMap<>();

    @FXML
    public void initialize() {
        monthComboBox.getItems().addAll(
                "All Months",
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );

        String currentMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        monthComboBox.setValue(currentMonth);
        loadDashboardData(currentMonth);

        monthComboBox.setOnAction(event -> {
            String selectedMonth = monthComboBox.getValue();
            loadDashboardData(selectedMonth);
        });

        monthComboBox.setOnMouseEntered(e -> monthComboBox.setStyle("-fx-background-color: #9145f5;"));
        monthComboBox.setOnMouseExited(e -> monthComboBox.setStyle("-fx-border-color: #732bb5; -fx-background-color: white;"));
    }

    private void loadDashboardData(String month) {
        if ("All Months".equals(month)) {
            loadPieChartData(null);
            loadBarChartData(null);
            updateTotalEarningsLabel(null);
        } else {
            loadPieChartData(month);
            loadBarChartData(month);
            updateTotalEarningsLabel(month);
        }
    }

    private void loadPieChartData(String month) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "SELECT Amount, COUNT(*) as Count, SUM(Amount) as TotalAmount " +
                    "FROM UserTransactions " +
                    (month != null ? "WHERE MONTH(DateTime) = " + getMonthNumber(month) + " " : "") +
                    "GROUP BY Amount";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                double amount = rs.getDouble("Amount");
                int count = rs.getInt("Count");
                double totalAmount = rs.getDouble("TotalAmount");

                String label = "";
                Color color = Color.GRAY;

                if (amount == 1000) {
                    label = "1 Month - " + "₱" + totalAmount + " - " + count + " Subscribed";
                    color = Color.RED;
                } else if (amount == 2500) {
                    label = "3 Months - " + "₱" + totalAmount + " - " + count + " Subscribed";
                    color = Color.GOLD;
                } else if (amount == 5000) {
                    label = "6 Months - " + "₱" + totalAmount + " - " + count + " Subscribed";
                    color = Color.GREEN;
                } else if (amount == 9500) {
                    label = "1 Year - " + "₱" + totalAmount + " - " + count + " Subscribed";
                    color = Color.BLUE;
                }

                colorMap.put(label.split(" - ")[0], color); // Save the color for the BarChart
                PieChart.Data slice = new PieChart.Data(label, count);
                pieChartData.add(slice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        membershipPieChart.setData(pieChartData);

        for (PieChart.Data data : membershipPieChart.getData()) {
            String key = data.getName().split(" - ")[0];
            Color color = colorMap.get(key);

            data.getNode().setStyle("-fx-pie-color: " + toHex(color) + ";");

            Tooltip tooltip = new Tooltip(data.getName());
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private void loadBarChartData(String month) {
        membershipBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "SELECT Amount, SUM(Amount) as TotalAmount " +
                    "FROM UserTransactions " +
                    (month != null ? "WHERE MONTH(DateTime) = " + getMonthNumber(month) + " " : "") +
                    "GROUP BY Amount";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                double amount = rs.getDouble("Amount");
                double totalAmount = rs.getDouble("TotalAmount");
                String label = "";

                if (amount == 1000) {
                    label = "1 Month";
                } else if (amount == 2500) {
                    label = "3 Months";
                } else if (amount == 5000) {
                    label = "6 Months";
                } else if (amount == 9500) {
                    label = "1 Year";
                }

                XYChart.Data<String, Number> data = new XYChart.Data<>(label, totalAmount);
                series.getData().add(data);

                Color color = colorMap.get(label);
                data.nodeProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        newValue.setStyle("-fx-bar-fill: " + toHex(color) + ";");
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        membershipBarChart.getData().add(series);
    }

    private void updateTotalEarningsLabel(String month) {
        double totalEarnings = 0;

        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "SELECT SUM(Amount) as TotalEarnings " +
                    "FROM UserTransactions " +
                    (month != null ? "WHERE MONTH(DateTime) = " + getMonthNumber(month) : "");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                totalEarnings = rs.getDouble("TotalEarnings");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        totalEarningsLabel.setText("₱" + totalEarnings);
    }

    private int getMonthNumber(String month) {
        return LocalDate.parse("2023-" + month + "-01", java.time.format.DateTimeFormatter.ofPattern("yyyy-MMMM-dd", Locale.ENGLISH))
                .getMonthValue();
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}