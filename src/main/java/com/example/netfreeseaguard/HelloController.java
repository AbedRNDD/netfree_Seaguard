package com.example.netfreeseaguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void onLoginClick() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isBlank() || password.isBlank()) {
            errorLabel.setText("Please enter email and password.");
            return;
        }

        // 1) Admin account => beheerder
        if (email.equals("admin@admin.com") && password.equals("1234")) {
            UserSession.setCurrentRole("beheerder");
            System.out.println("Logged in as ADMIN: " + email);
            goToDashboard();
            return;
        }

        // 2) Normal user must match the last signed-up account => visser
        if (UserSession.matchesRegisteredUser(email, password)) {
            UserSession.setCurrentRole("visser");
            System.out.println("Logged in as visser: " + email);
            goToDashboard();
            return;
        }

        // 3) Otherwise: invalid
        errorLabel.setText("Wrong email or password (or not signed up).");
    }


    @FXML
    private void onSignUpClick() {   // <-- name matches FXML now
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("signup.fxml")
            );
            Scene scene = new Scene(loader.load(), 360, 720);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Account aanmaken");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void goToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("root.fxml")
            );
            Scene scene = new Scene(loader.load(), 360, 720);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ghost Net Tracker");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load main layout.");
        }
    }
}