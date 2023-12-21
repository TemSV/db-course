package db.controllers;

import db.entity.Goods;
import db.entity.Sales;
import db.entity.Warehouse1;
import db.entity.Warehouse2;
import db.hebirnate.HibernateUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MainController {


    @FXML
    private TableView<Goods> goodsTableView;
    @FXML
    private TableColumn<Goods, Integer> idColumn;
    @FXML
    private TableColumn<Goods, String> nameColumn;
    @FXML
    private TableColumn<Goods, Integer> priorityColumn;
    @FXML
    private TableView<Sales> salesTableView;
    @FXML
    private TableColumn<Sales, Integer> salesIdColumn;
    @FXML
    private TableColumn<Sales, Integer> goodIdColumn;
    @FXML
    private TableColumn<Sales, Integer> goodCountColumn;
    @FXML
    private TableColumn<Sales, LocalDate> createDateColumn;
    @FXML
    private TableView<Warehouse1> warehouse1TableView;
    @FXML
    private TableColumn<Warehouse1, Integer> warehouse1IdColumn;
    @FXML
    private TableColumn<Warehouse1, Integer> warehouse1GoodIdColumn;
    @FXML
    private TableColumn<Warehouse1, Integer> warehouse1GoodCountColumn;
    @FXML
    private TableView<Warehouse2> warehouse2TableView;
    @FXML
    private TableColumn<Warehouse2, Integer> warehouse2IdColumn;
    @FXML
    private TableColumn<Warehouse2, Integer> warehouse2GoodIdColumn;
    @FXML
    private TableColumn<Warehouse2, Integer> warehouse2GoodCountColumn;
    @FXML
    private TextArea newGoodNameTextArea;
    @FXML
    private TextArea newGoodPriorityTextArea;
    @FXML
    private DatePicker addSaleCreateDate;
    @FXML
    private TextField addSaleGoodCount;
    @FXML
    private TextField addSaleGoodId;
    @FXML
    private TextField warehouse1NewGoodId;
    @FXML
    private TextField warehouse1NewGoodCount;
    @FXML
    private TextField warehouse2NewGoodId;
    @FXML
    private TextField warehouse2NewGoodCount;
    @FXML
    private TextField idForGraphic;
    @FXML
    private DatePicker sinceDatePicker;
    @FXML
    private DatePicker byDatePicker;
    @FXML
    private TextArea mostPopularGoods;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;



    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        ObservableList<Goods> goods = FXCollections.observableArrayList(HibernateUtil.getAllGoods());
        goodsTableView.setItems(goods);

        salesIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        goodIdColumn.setCellValueFactory(data -> {
            Goods goodsObj = data.getValue().getGoods();
            SimpleIntegerProperty goodIdProperty = new SimpleIntegerProperty(goodsObj.getId());
            return goodIdProperty.asObject();
        });
        goodCountColumn.setCellValueFactory(new PropertyValueFactory<>("goodCount"));
        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));

        ObservableList<Sales> sales = FXCollections.observableArrayList(HibernateUtil.getSales());
        salesTableView.setItems(sales);

        warehouse1IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        warehouse1GoodIdColumn.setCellValueFactory(data -> {
            Goods goodsObj = data.getValue().getGoods();
            SimpleIntegerProperty goodIdProperty = new SimpleIntegerProperty(goodsObj.getId());
            return goodIdProperty.asObject();
        });
        warehouse1GoodCountColumn.setCellValueFactory(new PropertyValueFactory<>("goodCount"));
        ObservableList<Warehouse1> warehouse1 = FXCollections.observableArrayList(HibernateUtil.getWarehouse1());
        warehouse1TableView.setItems(warehouse1);

        warehouse2IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        warehouse2GoodIdColumn.setCellValueFactory(data -> {
            Goods goodsObj = data.getValue().getGoods();
            SimpleIntegerProperty goodIdProperty = new SimpleIntegerProperty(goodsObj.getId());
            return goodIdProperty.asObject();
        });
        warehouse2GoodCountColumn.setCellValueFactory(new PropertyValueFactory<>("goodCount"));
        ObservableList<Warehouse2> warehouse2 = FXCollections.observableArrayList(HibernateUtil.getWarehouse2());
        warehouse2TableView.setItems(warehouse2);
    }


    @FXML
    void addNewGood(ActionEvent event) {
        String name = newGoodNameTextArea.getText();
        try {
            Integer priority = Integer.parseInt(newGoodPriorityTextArea.getText());
            Goods newGood = Goods.builder().name(name).priority(priority).build();
            HibernateUtil.addGood(newGood);
            initialize();
            newGoodNameTextArea.clear();
            newGoodPriorityTextArea.clear();
        } catch (NumberFormatException e) {
            Alerts.createInvalidFormatAlert();
        }
    }

    @FXML
    void editGood(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/editGoodPopupView.fxml"));
                Parent root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setTitle("Edit Good");
                dialogStage.isMaximized();
                dialogStage.setResizable(false);
                dialogStage.setScene(new Scene(root));
                EditGoodPopupController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                dialogStage.showAndWait();

                String name = controller.getEditedName();
                Integer priority = controller.getEditedPriority();
                if (name != null && priority != null) {
                    HibernateUtil.updateGood(goodsTableView.getSelectionModel().getSelectedItem(), name, priority);
                    initialize();
                }
                if (controller.isDelete()) {
                     if (HibernateUtil.deleteGood(goodsTableView.getSelectionModel().getSelectedItem())) {
                        initialize();
                     }
                    else  {
                         Alerts.createReferencesAlert();
                     }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void addSale(ActionEvent event) {
        try {
            Integer goodCount = Integer.parseInt(addSaleGoodCount.getText());
            Integer goodId = Integer.parseInt(addSaleGoodId.getText());
            Goods good = HibernateUtil.getGood(goodId);
            if (good != null) {
                LocalDate createDate = addSaleCreateDate.getValue();
                if (createDate != null) {
                    Sales newSale = Sales.builder()
                            .goods(good).goodCount(goodCount).createDate(createDate).build();
                    if (HibernateUtil.addSale(newSale)) {
                        initialize();
                        addSaleGoodId.clear();
                        addSaleGoodCount.clear();
                    }
                    else {
                        Alerts.createCanNotAddSaleAlert();
                    }
                }
                else {
                    Alerts.createInvalidFormatAlert();
                }
            }
            else {
                Alerts.createInvalidGoodIdAlert();
            }

        } catch (NumberFormatException e) {
            Alerts.createInvalidFormatAlert();
        }
    }


    @FXML
    void addGoodToWarehouse1(ActionEvent event) {
        try {
            Integer goodId = Integer.parseInt(warehouse1NewGoodId.getText());
            Integer goodCount = Integer.parseInt(warehouse1NewGoodCount.getText());
            Goods newGood = HibernateUtil.getGood(goodId);
            if (newGood != null) {
                Warehouse1 warehouse1Item = Warehouse1.builder().goods(newGood).goodCount(goodCount).build();
                HibernateUtil.addToWarehouse1(warehouse1Item);
                initialize();
            }
            else {
                Alerts.createInvalidGoodIdAlert();
            }
        } catch (NumberFormatException e) {
            Alerts.createInvalidFormatAlert();
        }
    }

    @FXML
    private void editWarehouse1(javafx.scene.input.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/editWarehousePopupView.fxml"));
                Parent root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setTitle("Edit Good");
                dialogStage.isMaximized();
                dialogStage.setResizable(false);
                dialogStage.setScene(new Scene(root));
                EditWarehousePopupViewController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                dialogStage.showAndWait();

                Integer newGoodCount = controller.getGoodCount();
                if (newGoodCount != null) {
                    HibernateUtil.updateWarehouse1(warehouse1TableView.getSelectionModel().getSelectedItem(), newGoodCount);
                    initialize();
                }
                if (controller.isDelete()) {
                    if (HibernateUtil.deleteInWarehouse1(warehouse1TableView.getSelectionModel().getSelectedItem())) {
                        initialize();
                    }
                    else  {
                        Alerts.createReferencesAlert();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void addGoodToWarehouse2(ActionEvent event) {
        try {
            Integer goodId = Integer.parseInt(warehouse2NewGoodId.getText());
            Integer goodCount = Integer.parseInt(warehouse2NewGoodCount.getText());
            Goods newGood = HibernateUtil.getGood(goodId);
            if (newGood != null) {
                Warehouse2 warehouse2Item = Warehouse2.builder().goods(newGood).goodCount(goodCount).build();
                HibernateUtil.addToWarehouse2(warehouse2Item);
                initialize();
            }
            else {
                Alerts.createInvalidGoodIdAlert();
            }
        } catch (NumberFormatException e) {
            Alerts.createInvalidFormatAlert();
        }
    }

    @FXML
    private void editWarehouse2(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/editWarehousePopupView.fxml"));
                Parent root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setTitle("Edit Good");
                dialogStage.isMaximized();
                dialogStage.setResizable(false);
                dialogStage.setScene(new Scene(root));
                EditWarehousePopupViewController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                dialogStage.showAndWait();

                Integer newGoodCount = controller.getGoodCount();
                if (newGoodCount != null) {
                    if (HibernateUtil.updateWarehouse2(warehouse2TableView.getSelectionModel().getSelectedItem(), newGoodCount)) {
                        initialize();
                    }
                    else {
                        Alerts.createCanNotDecreaseAlert();
                    }
                }
                if (controller.isDelete()) {
                    if (HibernateUtil.deleteInWarehouse2(warehouse2TableView.getSelectionModel().getSelectedItem())) {
                        initialize();
                    }
                    else  {
                        Alerts.createReferencesAlert();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void getMostPopularGoods(ActionEvent event) {
        LocalDate since =  sinceDatePicker.getValue();
        LocalDate by = byDatePicker.getValue();
        List<Object[]> top = HibernateUtil.getMostPopularGoods(since, by);
        StringBuilder finalResult = new StringBuilder("Top 5 goods:\n");
        int i = 1;
        for (Object[] row : top) {
            finalResult.append(i).append(". ").append(row[1]).append("\n");
            i++;
        }
        mostPopularGoods.setText(finalResult.toString());
    }


    @FXML
    private void drawGraphic(ActionEvent event) {
        try {
            Integer id = Integer.parseInt(idForGraphic.getText());
            List<Object[]> resultList = HibernateUtil.getDatesAndGoodCount(id);
            ObservableList<Object[]> result = FXCollections.observableArrayList(resultList);

            ObservableList<XYChart.Data<String, Number>> chartData = FXCollections.observableArrayList();
            for (Object[] row : result) {
                String xValue = row[0].toString();
                Number yValue = (Number) row[1];

                chartData.add(new XYChart.Data<>(xValue, yValue));
            }

            if (lineChart == null) {
                lineChart = new LineChart<String, Number>(xAxis, yAxis);
            }

            lineChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setData(chartData);
            lineChart.getData().add(series);
        } catch (NumberFormatException e) {
            Alerts.createInvalidFormatAlert();
        }

    }
}
