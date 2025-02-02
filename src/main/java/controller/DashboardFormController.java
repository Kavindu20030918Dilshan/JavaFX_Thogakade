package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class DashboardFormController {
    public AnchorPane loadFormContent;

    public void btnCustomerFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/customer_form.fxml");

        assert resource != null;
        Parent load = FXMLLoader.load(resource);

        loadFormContent.getChildren().clear();
        loadFormContent.getChildren().add(load);

    }

    public void btnItemFormOnAction(ActionEvent actionEvent) {
    }

    public void btnOrderFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/order_form.fxml");

        assert resource != null;
        Parent load = FXMLLoader.load(resource);

        loadFormContent.getChildren().clear();
        loadFormContent.getChildren().add(load);
    }

    public void btnRegisterFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/register_form.fxml");

        assert resource != null;
        Parent load = FXMLLoader.load(resource);

        loadFormContent.getChildren().clear();
        loadFormContent.getChildren().add(load);
    }
}
