package controller.customer;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws SQLException, IOException {

        String key = "#123*5";
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);

        String SQL = "SELECT * FROM users WHERE email="+"'"+txtEmail.getText()+"' ";
        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(SQL);

        if (resultSet.next()){

            User user = new User(
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );



            if (basicTextEncryptor.decrypt(user.getPassword()).equals(txtPassword.getText())){
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
                stage.show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Invalid password").show();
            }



        }else{
            new Alert(Alert.AlertType.ERROR,"User not found").show();
        }
    }

    @FXML
    void hyperRegisteredOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/register_form.fxml"))));
        stage.show();
    }

}
