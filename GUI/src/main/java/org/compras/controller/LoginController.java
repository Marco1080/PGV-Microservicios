package org.compras.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import org.compras.HelloApplication;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> {
            try {
                HelloApplication.showProductView();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}

