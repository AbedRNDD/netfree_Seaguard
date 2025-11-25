package com.example.netfreeseaguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    // --- Data model ---
    private static class Report {
        final int id;
        final double latPercent;
        final double lngPercent;
        final String username;
        final String date;
        final String location;
        final boolean isOwn;

        Report(int id,
               double latPercent,
               double lngPercent,
               String username,
               String date,
               String location,
               boolean isOwn) {
            this.id = id;
            this.latPercent = latPercent;
            this.lngPercent = lngPercent;
            this.username = username;
            this.date = date;
            this.location = location;
            this.isOwn = isOwn;
        }
    }

    private final List<Report> allReports = List.of(
            new Report(1, 40, 30, "OceanGuard21", "2025-10-12", "Pacific Ocean", true),
            new Report(2, 45, 35, "SeaProtector", "2025-10-13", "Atlantic Ocean", false),
            new Report(3, 50, 40, "WaveWatcher", "2025-10-14", "Indian Ocean", false),
            new Report(4, 35, 25, "OceanGuard21", "2025-10-10", "Mediterranean Sea", true),
            new Report(5, 55, 45, "NetHunter", "2025-10-11", "North Sea", false),
            new Report(6, 42, 28, "OceanGuard21", "2025-10-09", "Aegean Sea", true)
    );

    // --- FXML fields ---
    @FXML private Pane mapPane;
    @FXML private ToggleButton mijnTrackersToggle;
    @FXML private ToggleButton wereldKaartToggle;

    @FXML private VBox detailPanel;
    @FXML private Label detailInfoLabel;
    @FXML private Label detailLocationLabel;

    @FXML private Label totalLabel;
    @FXML private Label totalLabelCaption;
    @FXML private Label activeLabel;
    @FXML private Label monthLabel;

    @FXML private Label statsNavLabel;  // 🔴 from dashboard.fxml fx:id="statsNavLabel"

    // --- State ---
    private String viewMode = "own";
    private double zoom = 1.0;

    // --- Role ---
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
            statsNavLabel.setText("beheerder".equals(userRole) ? "Beheer" : "Stats");
        }

        viewMode = "own";
        refreshMap();
        updateStats();
        hideDetailPanel();
        updateViewToggleStyles();
    }

    // ============== view mode ==============

    @FXML
    private void onViewModeOwn() {
        viewMode = "own";
        refreshMap();
        updateStats();
        hideDetailPanel();
        updateViewToggleStyles();
    }

    @FXML
    private void onViewModeGlobal() {
        viewMode = "global";
        refreshMap();
        updateStats();
        hideDetailPanel();
        updateViewToggleStyles();
    }

    private void updateViewToggleStyles() {
        if (mijnTrackersToggle == null || wereldKaartToggle == null) return;

        mijnTrackersToggle.getStyleClass().setAll("view-toggle-btn");
        wereldKaartToggle.getStyleClass().setAll("view-toggle-btn");

        if ("own".equals(viewMode)) {
            mijnTrackersToggle.getStyleClass().add("view-toggle-btn-active");
            totalLabelCaption.setText("Mijn Nets");
        } else {
            wereldKaartToggle.getStyleClass().add("view-toggle-btn-active");
            totalLabelCaption.setText("Totaal");
        }
    }

    private List<Report> getDisplayedReports() {
        if ("own".equals(viewMode)) {
            return allReports.stream()
                    .filter(r -> r.isOwn)
                    .collect(Collectors.toList());
        }
        return allReports;
    }

    // ============== map ==============

    private void refreshMap() {
        mapPane.getChildren().clear();

        double width = mapPane.getPrefWidth();
        double height = mapPane.getPrefHeight();

        for (Report r : getDisplayedReports()) {
            Circle marker = new Circle(8, Color.web("#ff6b6b"));
            marker.setStroke(Color.web("#dc2626"));
            marker.setStrokeWidth(1.5);

            marker.setLayoutX(width * (r.lngPercent / 100.0));
            marker.setLayoutY(height * (r.latPercent / 100.0));

            marker.setOnMouseClicked(e -> showDetailPanel(r));

            mapPane.getChildren().add(marker);
        }

        applyZoom();
    }

    // ============== zoom ==============

    @FXML
    private void onZoomIn() {
        zoom = Math.min(zoom + 0.2, 2.0);
        applyZoom();
    }

    @FXML
    private void onZoomOut() {
        zoom = Math.max(zoom - 0.2, 0.6);
        applyZoom();
    }

    private void applyZoom() {
        mapPane.setScaleX(zoom);
        mapPane.setScaleY(zoom);
    }

    // ============== detail panel ==============

    private void showDetailPanel(Report r) {
        detailPanel.setVisible(true);
        detailPanel.setManaged(true);

        detailInfoLabel.setText("Reported by " + r.username + " on " + r.date);
        detailLocationLabel.setText(r.location);
    }

    @FXML
    private void onClosePanel() {
        hideDetailPanel();
    }

    private void hideDetailPanel() {
        detailPanel.setVisible(false);
        detailPanel.setManaged(false);
    }

    // ============== stats ==============

    private void updateStats() {
        List<Report> list = getDisplayedReports();
        totalLabel.setText(String.valueOf(list.size()));

        if ("own".equals(viewMode)) {
            activeLabel.setText("3");
            monthLabel.setText("2");
        } else {
            activeLabel.setText("1.2k");
            monthLabel.setText("89");
        }
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
        openScreen("report.fxml", "Meld Ghost Net");
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
            Stage stage = (Stage) mapPane.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
