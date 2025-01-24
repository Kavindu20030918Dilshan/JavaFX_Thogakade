package controller.customer;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import org.jasypt.util.text.BasicTextEncryptor;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterFormController extends Component {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtPasswordConfirm;

    @FXML
    private TextField txtUsername;

    @FXML
    void btnRegisterOnAction(ActionEvent event)throws SQLException
    {
        String key = "#123*5";
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);

        String SQL = "INSERT INTO users (username,email,password) VALUES(?,?,?)";

        if (txtPassword.getText().equals(txtPasswordConfirm.getText())){

            Connection connection = DBConnection.getInstance().getConnection();

            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM users WHERE email = " + "'" + txtEmail.getText() + "' ");

            if (resultSet.next()){
                new Alert(Alert.AlertType.ERROR,"User already registered").show();
            }else{



                User user = new User(
                        txtUsername.getText(),
                        txtEmail.getText(),
                        txtPassword.getText()
                );




                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1,user.getUsername());
                preparedStatement.setString(2,user.getEmail());
                preparedStatement.setString(3,basicTextEncryptor.encrypt(user.getPassword()));
                preparedStatement.executeUpdate();
            }

        }else{
            JOptionPane.showMessageDialog(this,"Password must be same","Warning",JOptionPane.WARNING_MESSAGE);

        }

    }

}
