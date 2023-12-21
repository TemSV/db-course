package db.controllers;

import db.Main;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditGoodPopupController {

    @FXML
    private Button saveButton;
    private Stage dialogStage;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priorityField;

    private boolean delete;

    private String editedName;

    private Integer editedPriority;


    @FXML
    private void save(Event event) {
        try {
            if (nameField.getText() != null) {
                editedName = nameField.getText();
                editedPriority = Integer.parseInt(priorityField.getText());
                dialogStage.close();
            }
            else {
                Alerts.createInvalidFormatAlert();
            }
        } catch (NumberFormatException e) {
            Alerts.createInvalidFormatAlert();
        }
    }

    @FXML
    private void delete(Event event) {
        if (Main.getLoginController().getSecurityManager().isAdmin(Main.getLoginController().getUserName())) {
            delete = true;
            dialogStage.close();
        }
        else {
            Alerts.createNotAdminAlert();
        }
    }

    @FXML
    private void cancel(Event event) {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public String getEditedName() {
        return editedName;
    }

    public Integer getEditedPriority() {
        return editedPriority;
    }


    public boolean isDelete() {
        return delete;
    }
}
