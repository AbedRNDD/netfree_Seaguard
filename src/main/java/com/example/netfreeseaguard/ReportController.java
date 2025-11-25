package com.example.netfreeseaguard;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ReportController {

    @FXML private AnchorPane root;

    @FXML private VBox formRoot;
    @FXML private VBox successRoot;

    @FXML private TextField locationField;
    @FXML private TextArea descriptionArea;

    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        if (statusLabel != null)
            statusLabel.setText("");
    }

    @FXML
    private void onSubmit() {
        System.out.println("Report submitted:");
        System.out.println("Location: " + locationField.getText());
        System.out.println("Description: " + descriptionArea.getText());

        showSuccess();
    }

    private void showSuccess() {
        formRoot.setVisible(false);
        formRoot.setManaged(false);

        successRoot.setVisible(true);
        successRoot.setManaged(true);

        PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
        delay.setOnFinished(e -> resetForm());
        delay.play();
    }

    private void resetForm() {
        formRoot.setVisible(true);
        formRoot.setManaged(true);

        successRoot.setVisible(false);
        successRoot.setManaged(false);

        descriptionArea.clear();
        statusLabel.setText("");
    }

    @FXML
    private void onBackClick() {
        openScreen("dashboard.fxml", "Ghost Net Tracker");
    }

    // ------- NAVIGATION -------

    @FXML
    private void onNavHome() { openScreen("dashboard.fxml", "Ghost Net Tracker"); }

    @FXML
    private void onNavReport() { openScreen("report.fxml", "Meld Ghost Net"); }

    @FXML
    private void onNavAlerts() { openScreen("notifications.fxml", "Meldingen"); }

    @FXML
    private void onNavStats() {
        String role = UserSession.getCurrentRole();
        if ("beheerder".equals(role))
            openScreen("manage.fxml", "Beheer Netten");
        else
            openScreen("stats.fxml", "Statistieken");
    }

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
