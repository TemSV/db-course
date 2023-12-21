package db.controllers;

import javafx.scene.control.Alert;

public class Alerts {
    public static Alert createNotAdminAlert() {
        Alert notAdminAlert = new Alert(Alert.AlertType.ERROR);
        notAdminAlert.setHeaderText("Can't delete");
        notAdminAlert.setContentText("You must be an admin!");
        notAdminAlert.showAndWait();
        return notAdminAlert;
    }

    public static Alert createInvalidFormatAlert() {
        Alert invalidFormatAlert = new Alert(Alert.AlertType.ERROR);
        invalidFormatAlert.setHeaderText("Can't save");
        invalidFormatAlert.setContentText("Invalid format!");
        invalidFormatAlert.showAndWait();
        return invalidFormatAlert;
    }

    public static Alert createReferencesAlert() {
        Alert referencesAlert = new Alert(Alert.AlertType.ERROR);
        referencesAlert.setHeaderText("Can not delete");
        referencesAlert.setContentText("This good has references in sales!");
        referencesAlert.showAndWait();
        return referencesAlert;
    }

    public static Alert createInvalidGoodIdAlert() {
        Alert invalidGoodIdAlert = new Alert(Alert.AlertType.ERROR);
        invalidGoodIdAlert.setHeaderText("Invalid id");
        invalidGoodIdAlert.setContentText("There is no such good in table goods!");
        invalidGoodIdAlert.showAndWait();
        return invalidGoodIdAlert;
    }

    public static Alert createCanNotAddSaleAlert() {
        Alert canNotAddSaleAlert = new Alert(Alert.AlertType.ERROR);
        canNotAddSaleAlert.setHeaderText("Can not add sale");
        canNotAddSaleAlert.setContentText("Provided good count is bigger than exists on warehouses!");
        canNotAddSaleAlert.showAndWait();
        return canNotAddSaleAlert;
    }

    public static Alert createCanNotDecreaseAlert() {
        Alert canNotDecreaseAlert = new Alert(Alert.AlertType.ERROR);
        canNotDecreaseAlert.setHeaderText("Can not decrease");
        canNotDecreaseAlert
                .setContentText("Can not decrease good count in warehouse2 because its good count in warehouse1 is not 0");
        canNotDecreaseAlert.showAndWait();
        return canNotDecreaseAlert;
    }
}
