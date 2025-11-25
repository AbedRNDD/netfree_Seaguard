package com.example.netfreeseaguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class StatsController {

    // Top cards
    @FXML private Label totalReportsLabel;
    @FXML private Label totalRecoveredLabel;
    @FXML private Label recoveryRateLabel;
    @FXML private Label activeNetsLabel;
    @FXML private Label ratioLabel;

    // Charts
    @FXML private LineChart<String, Number> trendChart;
    @FXML private BarChart<String, Number> monthlyChart;

    // Distribution
    @FXML private Label pacificCountLabel;
    @FXML private Label atlanticCountLabel;
    @FXML private Label indianCountLabel;
    @FXML private Label medCountLabel;

    @FXML private ProgressBar pacificProgress;
    @FXML private ProgressBar atlanticProgress;
    @FXML private ProgressBar indianProgress;
    @FXML private ProgressBar medProgress;

    @FXML
    private void initialize() {
        // dummy numbers for now
        int total = 120;
        int recovered = 80;
        int active = total - recovered;

        if (totalReportsLabel != null) totalReportsLabel.setText(String.valueOf(total));
        if (totalRecoveredLabel != null) totalRecoveredLabel.setText(String.valueOf(recovered));
        if (activeNetsLabel != null) activeNetsLabel.setText(String.valueOf(active));

        double ratio = total == 0 ? 0 : (recovered * 100.0 / total);
        if (recoveryRateLabel != null) recoveryRateLabel.setText(String.format("%.0f%% ratio", ratio));
        if (ratioLabel != null) ratioLabel.setText(String.format("%.0f%%", ratio));

        setupTrendChart();
        setupMonthlyChart();
        setupDistribution();
    }

    private void setupTrendChart() {
        if (trendChart == null) return;

        trendChart.getData().clear();

        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.getData().add(new XYChart.Data<>("Mei", 35));
        s.getData().add(new XYChart.Data<>("Jun", 32));
        s.getData().add(new XYChart.Data<>("Jul", 30));
        s.getData().add(new XYChart.Data<>("Aug", 28));
        s.getData().add(new XYChart.Data<>("Sep", 25));
        s.getData().add(new XYChart.Data<>("Okt", 23));

        trendChart.getData().add(s);
    }

    private void setupMonthlyChart() {
        if (monthlyChart == null) return;

        monthlyChart.getData().clear();

        XYChart.Series<String, Number> reported = new XYChart.Series<>();
        reported.setName("Gemeld");
        reported.getData().add(new XYChart.Data<>("Mei", 20));
        reported.getData().add(new XYChart.Data<>("Jun", 24));
        reported.getData().add(new XYChart.Data<>("Jul", 26));
        reported.getData().add(new XYChart.Data<>("Aug", 28));
        reported.getData().add(new XYChart.Data<>("Sep", 22));
        reported.getData().add(new XYChart.Data<>("Okt", 25));

        XYChart.Series<String, Number> cleaned = new XYChart.Series<>();
        cleaned.setName("Opgeruimd");
        cleaned.getData().add(new XYChart.Data<>("Mei", 10));
        cleaned.getData().add(new XYChart.Data<>("Jun", 14));
        cleaned.getData().add(new XYChart.Data<>("Jul", 18));
        cleaned.getData().add(new XYChart.Data<>("Aug", 20));
        cleaned.getData().add(new XYChart.Data<>("Sep", 19));
        cleaned.getData().add(new XYChart.Data<>("Okt", 21));

        monthlyChart.getData().addAll(reported, cleaned);
    }

    private void setupDistribution() {
        // just fixed values for now
        int pacific = 42;
        int atlantic = 31;
        int indian = 24;
        int med = 13;
        int total = pacific + atlantic + indian + med;

        if (pacificCountLabel != null) pacificCountLabel.setText(pacific + " nets");
        if (atlanticCountLabel != null) atlanticCountLabel.setText(atlantic + " nets");
        if (indianCountLabel != null) indianCountLabel.setText(indian + " nets");
        if (medCountLabel != null) medCountLabel.setText(med + " nets");

        if (total == 0) total = 1; // avoid divide by zero

        if (pacificProgress != null) pacificProgress.setProgress(pacific / (double) total);
        if (atlanticProgress != null) atlanticProgress.setProgress(atlantic / (double) total);
        if (indianProgress != null) indianProgress.setProgress(indian / (double) total);
        if (medProgress != null) medProgress.setProgress(med / (double) total);
    }

    // ========== NAVIGATION ==========

    private void openScreen(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(fxml)
            );
            Scene scene = new Scene(loader.load(), 360, 720);
            Stage stage = (Stage) monthlyChart.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNavHome() {
        openScreen("dashboard.fxml", "Ghost Net Tracker");
    }

    @FXML
    private void onNavReport() {
        openScreen("report.fxml", "Meld Ghost Net");
    }

    @FXML
    private void onNavAlerts() {
        openScreen("notifications.fxml", "Meldingen");
    }

    @FXML
    private void onNavStats() {
        // already here, but keep method for FXML
        System.out.println("Nav: Stats (already on stats screen)");
    }

    @FXML
    private void onMenuClick() {
        openScreen("profile.fxml", "Profiel");
    }
}
