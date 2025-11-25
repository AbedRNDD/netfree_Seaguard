package com.example.netfreeseaguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ProfileController {

    @FXML
    private Label totalReportsLabel;

    @FXML
    private Label locationsCountLabel;

    @FXML
    private void initialize() {
        int totalReports = 12;
        int locations = 4;

        if (totalReportsLabel != null) {
            totalReportsLabel.setText(String.valueOf(totalReports));
        }
        if (locationsCountLabel != null) {
            locationsCountLabel.setText(locations + " locaties");
        }
    }

    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("dashboard.fxml")
            );
            Scene scene = new Scene(loader.load(), 360, 720);
            Stage stage = (Stage) totalReportsLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ghost Net Tracker");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLogoutClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("hello-view.fxml")
            ); // change name if your login fxml is different
            Scene scene = new Scene(loader.load(), 360, 720);
            Stage stage = (Stage) totalReportsLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("NetFree SeaGuard – Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
