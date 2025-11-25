package com.example.netfreeseaguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NotificationsController {

    @FXML
    private AnchorPane root;

    @FXML
    private Label activeBadgeLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private void initialize() {
        // Static for now – you can later compute based on actual data
        if (activeBadgeLabel != null) {
            activeBadgeLabel.setText("3 actief");
        }
        if (summaryLabel != null) {
            summaryLabel.setText("3 trackers hebben aandacht nodig");
        }
    }

    // Back arrow = same as Home
    @FXML
    private void onBackClick() {
        openScreen("dashboard.fxml", "Ghost Net Tracker");
    }

    // -------- Bottom nav handlers ----------

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
        // already here – do nothing or reload
        System.out.println("Already on Alerts");
    }

    @FXML
    private void onNavStats() {
        String role = UserSession.getCurrentRole();
        if ("beheerder".equals(role)) {
            openScreen("manage.fxml", "Beheer Netten");
        } else {
            openScreen("stats.fxml", "Statistieken");
        }
    }

    // Shared helper
    private void openScreen(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(fxml)
            );
            Scene scene = new Scene(loader.load(), 360, 720);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
