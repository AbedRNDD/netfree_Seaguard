package com.example.netfreeseaguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;

public class DashboardController {

    // --- FXML fields ---
    @FXML private WebView webView;
    @FXML private Label totalLabel;
    @FXML private Label totalLabelCaption;
    @FXML private Label activeLabel;
    @FXML private Label monthLabel;
    @FXML private Label statsNavLabel;

    // --- State ---
    private WebEngine webEngine;
    private String userRole;

    @FXML
    private void initialize() {
        // role from session
        userRole = UserSession.getCurrentRole();
        if (userRole == null) {
            userRole = "visser";
        }
        System.out.println("Dashboard loaded as: " + userRole);

        if (statsNavLabel != null) {
            statsNavLabel.setText("beheerder".equals(userRole) ? "Beheer" : "Statistieken");
        }

        // Initialize WebView with map
        webEngine = webView.getEngine();
        webView.setContextMenuEnabled(false);
        
        // إضافة listener لإعادة حساب حجم الخريطة بعد التحميل
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                // إعادة حساب حجم الخريطة بعد التحميل الكامل
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(400);
                        webEngine.executeScript("if(window.map) { window.map.invalidateSize(); }");
                    } catch (Exception e) {
                        // تجاهل الأخطاء
                    }
                });
            }
        });
        
        URL mapUrl = HelloApplication.class.getResource("map.html");
        if (mapUrl != null) {
            webEngine.load(mapUrl.toExternalForm());
        }

        updateStats();
    }

    // ============== zoom ==============

    @FXML
    private void onZoomIn() {
        if (webEngine != null) {
            webEngine.executeScript("if(window.zoomIn) window.zoomIn();");
        }
    }

    @FXML
    private void onZoomOut() {
        if (webEngine != null) {
            webEngine.executeScript("if(window.zoomOut) window.zoomOut();");
        }
    }

    // ============== stats ==============

    private void updateStats() {
        totalLabel.setText("6");
        totalLabelCaption.setText("Totaal Netten");
        activeLabel.setText("1.2k");
        monthLabel.setText("89");
    }

    // ============== nav ==============

    @FXML
    private void onNavHome() {
        // already on home
        System.out.println("Nav: home");
    }

    @FXML
    private void onNavAlerts() {
        openScreen("notifications.fxml", "Meldingen");
    }

    @FXML
    private void onNavStats() {
        if ("beheerder".equals(userRole)) {
            openScreen("manage.fxml", "Beheer Netten");
        } else {
            openScreen("stats.fxml", "Statistieken");
        }
    }

    @FXML
    private void onNavReport() {
        openScreen("report.fxml", "Meld Spooknet");
    }

    @FXML
    private void onMenuClick() {
        openScreen("profile.fxml", "Profiel");
    }

    private void openScreen(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(fxml)
            );
            Scene scene = new Scene(loader.load(), 360, 720);
            Stage stage = (Stage) webView.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
