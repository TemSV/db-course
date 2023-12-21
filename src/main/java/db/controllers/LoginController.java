package db.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import db.Main;
import db.security.SecurityManager;

import java.security.NoSuchAlgorithmException;

public class LoginController {

    private SecurityManager securityManager;

    @FXML
    private TextField usernameField;

    private String userName;

    @FXML
    private PasswordField passwordField;

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    void login(ActionEvent event) throws NoSuchAlgorithmException {
        securityManager = new SecurityManager();
        if (securityManager.logIn(usernameField.getText(), passwordField.getText())) {
            userName = usernameField.getText();
            mainApp.showMainScreen((Stage) usernameField.getScene().getWindow());
        }
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public String getUserName() {
        return userName;
    }
}
