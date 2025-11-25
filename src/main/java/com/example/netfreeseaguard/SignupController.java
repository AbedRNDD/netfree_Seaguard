package com.example.netfreeseaguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    // still injected from FXML, but we will force visser
    @FXML
    private CheckBox visserCheck;

    @FXML
    private CheckBox beheerderCheck;

    @FXML
    private void initialize() {
        // default: visser
        if (visserCheck != null) {
            visserCheck.setSelected(true);
        }
        // beheerder is NOT allowed through signup
        if (beheerderCheck != null) {
            beheerderCheck.setSelected(false);
            beheerderCheck.setDisable(true); // user can't select it
        }
    }

    // You can keep these handlers if they’re still referenced in FXML,
    // but they will always force "visser".
    @FXML
    private void onVisserChecked() {
        if (visserCheck != null) {
            visserCheck.setSelected(true);
        }
        if (beheerderCheck != null) {
            beheerderCheck.setSelected(false);
        }
    }

    @FXML
    private void onBeheerderChecked() {
        // prevent beheerder selection via signup
        if (beheerderCheck != null) {
            beheerderCheck.setSelected(false);
        }
        if (visserCheck != null) {
            visserCheck.setSelected(true);
        }
    }

    @FXML
    private void onCreateAccount() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email of wachtwoord leeg.");
            return;
        }

        // NEW: every signup is a visser
        String role = "visser";
        UserSession.registerUser(email, password);   // save credentials in memory
        UserSession.setCurrentRole(role);

        System.out.println("Account aangemaakt: " + email + " | Rol: " + role);

        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("dashboard.fxml")
            );
            Scene scene = new Scene(loader.load(), 360, 720);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("NetFree SeaGuard - " + role);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("hello-view.fxml")
            );
            Scene scene = new Scene(loader.load(), 360, 720);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("NetFree SeaGuard - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
