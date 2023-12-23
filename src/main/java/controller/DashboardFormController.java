package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardFormController {


    public AnchorPane pane;
    public Label lblTime;
    public Label lblDate;

    public void initialize(){
        calculateTime();
        calculateDate();

    }

    private void calculateDate() {
        LocalDate localDate=LocalDate.now();
        lblDate.setText(String.valueOf(localDate));
    }

    private void calculateTime() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.ZERO,
                actionEvent -> lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        ), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void customerButtonOnAction(ActionEvent actionEvent) {
        Stage stage=(Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"))));
            stage.setTitle("Customer Form");
            stage.show();
            stage.setResizable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void itemButtonOnAction(ActionEvent actionEvent) {
        Stage stage2=(Stage) pane.getScene().getWindow();
        try {
            stage2.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/ItemForm.fxml"))));
            stage2.setTitle("Item Form");
            stage2.setResizable(true);
            stage2.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void placeOrderButtonOnAction(ActionEvent actionEvent) {
        Stage stage= (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/PlaceOrderForm.fxml"))));
            stage.setTitle("PlaceOrderForm");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void ordersButtonOnAction(ActionEvent actionEvent) {
        Stage stage= (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/OrdersForm.fxml"))));
            stage.setTitle("orders Form");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
