package db.controllers;

import db.Main;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditWarehousePopupViewController {

    @FXML
    private void save(ActionEvent event) {
        try {
            goodCount = Integer.parseInt(editedGoodCount.getText());
            dialogStage.close();
        } catch (NumberFormatException e) {
            Alerts.createInvalidFormatAlert();
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        if (Main.getLoginController().getSecurityManager().isAdmin(Main.getLoginController().getUserName())) {
            delete = true;
            dialogStage.close();
        }
        else {
            Alerts.createNotAdminAlert();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private Stage dialogStage;

    @FXML
    private TextField editedGoodCount;

    private Integer goodCount;

    private boolean delete;

    public Integer getGoodCount() {
        return goodCount;
    }

    public boolean isDelete() {
        return delete;
    }


}
