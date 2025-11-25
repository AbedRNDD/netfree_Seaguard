package com.example.netfreeseaguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class RootController {

    @FXML
    private StackPane contentRoot;

    @FXML
    private void initialize() {
        // show dashboard by default after login
        loadScreen("dashboard.fxml");
    }

    // ---------- Navigation helpers ----------

    private void loadScreen(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("dashboard.fxml")
            );
            Node content = loader.load();
            contentRoot.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // top bar menu
    @FXML
    private void onMenuClick() {
        loadScreen("profile.fxml");
    }

    // bottom nav
    @FXML
    private void goHome() {
        loadScreen("dashboard.fxml");
    }

    @FXML
    private void goReport() {
        loadScreen("report.fxml");
    }

    @FXML
    private void goAlerts() {
        loadScreen("notifications.fxml");
    }

    @FXML
    private void goStats() {
        String role = UserSession.getCurrentRole();
        if ("beheerder".equals(role)) {
            loadScreen("manage.fxml");
        } else {
            loadScreen("stats.fxml");
        }
    }
}
