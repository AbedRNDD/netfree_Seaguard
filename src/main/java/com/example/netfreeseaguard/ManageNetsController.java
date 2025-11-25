package com.example.netfreeseaguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageNetsController {

    // Simple data model matching your TSX
    private static class Net {
        final String id;
        final String location;
        final String reportedBy;
        final String date;     // keep as String for now
        final String status;   // "active" or "recovered"

        Net(String id, String location, String reportedBy, String date, String status) {
            this.id = id;
            this.location = location;
            this.reportedBy = reportedBy;
            this.date = date;
            this.status = status;
        }
    }

    private final List<Net> nets = new ArrayList<>();

    // FXML fields
    @FXML
    private Label activeCountLabel;

    @FXML
    private Label recoveredCountLabel;

    @FXML
    private TextField searchField;

    @FXML
    private VBox addFormBox;

    @FXML
    private TextField netIdField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField reporterField;

    @FXML
    private VBox listContainer;

    @FXML
    private VBox emptyStateBox;

    // State
    private boolean addFormVisible = false;

    @FXML
    private void initialize() {
        // Seed mock data (same as in React)
        nets.add(new Net("NET-2847",
                "Noordzee, 52.3°N 4.2°E",
                "VisserJan",
                "2025-10-12",
                "active"));
        nets.add(new Net("NET-1923",
                "Waddenzee, 53.4°N 6.1°E",
                "OceanGuard21",
                "2025-10-11",
                "active"));
        nets.add(new Net("NET-3401",
                "IJsselmeer, 52.7°N 5.4°E",
                "SeaProtector",
                "2025-10-10",
                "recovered"));
        nets.add(new Net("NET-2156",
                "Markermeer, 52.5°N 5.2°E",
                "NetHunter",
                "2025-10-09",
                "active"));
        nets.add(new Net("NET-4512",
                "Atlantische Oceaan",
                "WaveWatcher",
                "2025-10-08",
                "active"));

        updateCounts();
        refreshList();
        setAddFormVisible(false);
    }

    // --- Navigation ---

    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("dashboard.fxml")
            );
            Scene scene = new Scene(loader.load(), 360, 720);
            Stage stage = (Stage) activeCountLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ghost Net Tracker");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Add form visibility ---

    @FXML
    private void onToggleAddForm() {
        setAddFormVisible(!addFormVisible);
    }

    private void setAddFormVisible(boolean visible) {
        addFormVisible = visible;
        if (addFormBox != null) {
            addFormBox.setVisible(visible);
            addFormBox.setManaged(visible);
        }
    }

    @FXML
    private void onCancelAdd() {
        clearAddForm();
        setAddFormVisible(false);
    }

    // --- Add net ---

    @FXML
    private void onAddNet() {
        String id = netIdField.getText().trim();
        String loc = locationField.getText().trim();
        String rep = reporterField.getText().trim();

        if (id.isEmpty() || loc.isEmpty() || rep.isEmpty()) {
            showError("Vul Net ID, locatie en melder in.");
            return;
        }

        // simple date: today
        String today = LocalDate.now().toString();

        nets.add(new Net(id, loc, rep, today, "active"));
        clearAddForm();
        setAddFormVisible(false);
        updateCounts();
        refreshList();
    }

    private void clearAddForm() {
        if (netIdField != null) netIdField.clear();
        if (locationField != null) locationField.clear();
        if (reporterField != null) reporterField.clear();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Ongeldige invoer");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // --- Search ---

    @FXML
    private void onSearchChanged() {
        refreshList();
    }

    private List<Net> getFilteredNets() {
        String q = (searchField != null && searchField.getText() != null)
                ? searchField.getText().toLowerCase()
                : "";

        if (q.isEmpty()) {
            return new ArrayList<>(nets);
        }

        return nets.stream()
                .filter(n ->
                        n.id.toLowerCase().contains(q) ||
                                n.location.toLowerCase().contains(q) ||
                                n.reportedBy.toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    // --- Counts ---

    private void updateCounts() {
        long active = nets.stream().filter(n -> "active".equals(n.status)).count();
        long recovered = nets.stream().filter(n -> "recovered".equals(n.status)).count();

        if (activeCountLabel != null) {
            activeCountLabel.setText(String.valueOf(active));
        }
        if (recoveredCountLabel != null) {
            recoveredCountLabel.setText(String.valueOf(recovered));
        }
    }

    // --- List rendering ---

    private void refreshList() {
        List<Net> filtered = getFilteredNets();

        listContainer.getChildren().clear();

        if (filtered.isEmpty()) {
            if (emptyStateBox != null) {
                emptyStateBox.setVisible(true);
                emptyStateBox.setManaged(true);
            }
            return;
        } else {
            if (emptyStateBox != null) {
                emptyStateBox.setVisible(false);
                emptyStateBox.setManaged(false);
            }
        }

        for (Net net : filtered) {
            listContainer.getChildren().add(createNetCard(net));
        }
    }

    private VBox createNetCard(Net net) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(8));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: rgba(6,182,212,0.2);" +
                        "-fx-border-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.1), 8, 0, 0, 2);"
        );

        // Title row: id + status + delete button
        HBox header = new HBox(6);
        header.setSpacing(6);

        Label idLabel = new Label(net.id);
        idLabel.setStyle("-fx-text-fill: #0c4a6e; -fx-font-size: 13; -fx-font-weight: bold;");

        Label statusLabel = new Label("active".equals(net.status) ? "Actief" : "Opgeruimd");
        if ("active".equals(net.status)) {
            statusLabel.setStyle(
                    "-fx-background-color: rgba(255,107,107,0.2);" +
                            "-fx-text-fill: #ff6b6b;" +
                            "-fx-font-size: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 2 6 2 6;"
            );
        } else {
            statusLabel.setStyle(
                    "-fx-background-color: rgba(34,211,238,0.2);" +
                            "-fx-text-fill: #22d3ee;" +
                            "-fx-font-size: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 2 6 2 6;"
            );
        }

        HBox leftHeader = new HBox(6, idLabel, statusLabel);
        leftHeader.setSpacing(6);

        // delete button
        Button deleteBtn = new Button("🗑");
        deleteBtn.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #ff6b6b; " +
                        "-fx-font-size: 12;"
        );
        deleteBtn.setOnAction(e -> confirmDelete(net));

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        header.getChildren().addAll(leftHeader, spacer, deleteBtn);

        // Details
        VBox details = new VBox(4);

        Label locLabel = new Label("📍 " + net.location);
        locLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11;");

        Label repLabel = new Label("👤 " + net.reportedBy);
        repLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11;");

        Label dateLabel = new Label("📅 " + net.date);
        dateLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11;");

        details.getChildren().addAll(locLabel, repLabel, dateLabel);

        card.getChildren().addAll(header, details);
        return card;
    }

    private void confirmDelete(Net net) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Net verwijderen");
        alert.setHeaderText("Net verwijderen?");
        alert.setContentText("Weet je zeker dat je " + net.id + " wilt verwijderen uit het systeem?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                nets.remove(net);
                updateCounts();
                refreshList();
            }
        });
    }
    // ================= NAVIGATION (top menu + bottom nav) =================

    @FXML
    private void onMenuClick() {
        // hamburger menu → profiel
        openScreen("profile.fxml", "Profiel");
    }

    @FXML
    private void onNavHome() {
        // home icon → dashboard/kaart
        openScreen("dashboard.fxml", "Ghost Net Tracker");
    }

    @FXML
    private void onNavReport() {
        // pencil icon → report screen
        openScreen("report.fxml", "Meld Ghost Net");
    }

    @FXML
    private void onNavAlerts() {
        // bell icon → notifications
        openScreen("notifications.fxml", "Meldingen");
    }

    @FXML
    private void onNavStats() {
        // chart icon → stats screen
        openScreen("stats.fxml", "Statistieken & Analyse");
    }

    // helper used by all the nav handlers above
    private void openScreen(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(fxml)
            );
            Scene scene = new Scene(loader.load(), 360, 720);

            // use any node from this screen to get the stage
            Stage stage = (Stage) activeCountLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
