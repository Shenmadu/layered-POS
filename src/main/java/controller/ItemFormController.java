package controller;

import bo.BoFactory;
import bo.custom.ItemBo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dao.util.BoType;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import dto.ItemDto;
import dto.tm.ItemTm;
import dao.custom.ItemDao;
import dao.custom.impl.ItemDaoImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.function.Predicate;


public class ItemFormController {


    public JFXButton goBackButton;
    @FXML
    private BorderPane pane;

    @FXML
    private JFXTextField txtCode;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtqtyOnHand;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTreeTableView<ItemTm> tblItem;

    @FXML
    private TreeTableColumn<?, ?> colCode;

    @FXML
    private TreeTableColumn<?, ?> colDescription;

    @FXML
    private TreeTableColumn<?, ?> colUnitPrice;

    @FXML
    private TreeTableColumn<?, ?> colQty;

    @FXML
    private TreeTableColumn<?, ?> colOption;
    private ItemBo itemBo = BoFactory.getInstance().getBo(BoType.ITEM);

    public void initialize() {
        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qtyOnHand"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        loardItemTable();

        tblItem.getSelectionModel().selectedItemProperty().addListener((observableValue, oldvalue, newValue) -> {
            setData(newValue);
        });

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
                tblItem.setPredicate(new Predicate<TreeItem<ItemTm>>() {
                    @Override
                    public boolean test(TreeItem<ItemTm> treeItem) {
                        return treeItem.getValue().getCode().contains(newValue) ||
                                treeItem.getValue().getDescription().contains(newValue);
                    }
                });
            }
        });
    }

    private void setData(TreeItem<ItemTm> newValue) {
        if (newValue != null) {
            ItemTm itemTm = newValue.getValue();
            txtCode.setEditable(false);
            txtCode.setText(itemTm.getCode());
            txtDescription.setText(itemTm.getDescription());
            txtUnitPrice.setText(String.valueOf(itemTm.getUnitPrice()));
            txtqtyOnHand.setText(String.valueOf(itemTm.getQtyOnHand()));
        }

    }

    private void loardItemTable() {
        ObservableList<ItemTm> tmList = FXCollections.observableArrayList();
        try {
            List<ItemDto> dtList = itemBo.allItems();
            for (ItemDto dto : dtList) {
                JFXButton btn = new JFXButton("Delete");
                btn.setStyle("-fx-background-color: red;");
                ItemTm i = new ItemTm(
                        dto.getCode(),
                        dto.getDescription(),
                        dto.getUnitPrice(),
                        dto.getQtyOnHand(),
                        btn
                );
                btn.setOnAction(actionEvent -> {
                    deleteItem(i.getCode());
                });
                tmList.add(i);
            }
            TreeItem<ItemTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
            tblItem.setRoot(treeItem);
            tblItem.setShowRoot(false);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteItem(String code) {
        try {
            boolean isDeletd = itemBo.deleteItem(code);
            if (isDeletd) {
                new Alert(Alert.AlertType.INFORMATION, "Item Deleted").show();
                loardItemTable();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void reloadButtonOnAction(ActionEvent event) {
        loardItemTable();
        tblItem.refresh();
        clearFields();
    }

    private void clearFields() {
        tblItem.refresh();
        txtqtyOnHand.clear();
        txtUnitPrice.clear();
        txtDescription.clear();
        txtCode.clear();
        txtCode.setEditable(true);

    }

    @FXML
    void saveButtonOnAction(ActionEvent event) {
        if (!(txtCode.getText().isEmpty() || txtDescription.getText().isEmpty() || txtUnitPrice.getText().isEmpty() || txtqtyOnHand.getText().isEmpty())) {
            try {
                boolean isSaved = itemBo.saveItem(new ItemDto(
                        txtCode.getText(),
                        txtDescription.getText(),
                        Double.parseDouble(txtUnitPrice.getText()),
                        Integer.parseInt(txtqtyOnHand.getText())
                ));

                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Item Saved").show();
                    loardItemTable();
                    clearFields();
                }

            } catch (SQLIntegrityConstraintViolationException ex) {
                new Alert(Alert.AlertType.ERROR, "Duplicate Entry").show();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please Enter the Data").show();
        }
    }

    @FXML
    void updateButtonOnAction(ActionEvent event) {
        if (!(txtCode.getText().isEmpty() || txtDescription.getText().isEmpty() || txtUnitPrice.getText().isEmpty() || txtqtyOnHand.getText().isEmpty())) {
            try {
                boolean isUpdated = itemBo.updateItem(new ItemDto(
                        txtCode.getText(),
                        txtDescription.getText(),
                        Double.parseDouble(txtUnitPrice.getText()),
                        Integer.parseInt(txtqtyOnHand.getText())
                ));
                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Item Updted").show();
                    loardItemTable();
                    clearFields();
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please Enter the Data").show();
        }
    }


    public void goBackButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) goBackButton.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void reportButtonOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign load =JRXmlLoader.load("src/main/resources/reports/itemReport.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);


        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}



