package controller.order;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerController;
import controller.item.ItemController;
import db.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {

    @FXML
    private JFXComboBox cmbCustomerId;

    @FXML
    private JFXComboBox cmbItemCode;

    @FXML
    private TableColumn colDesciption;

    @FXML
    private TableColumn colItemCode;

    @FXML
    private TableColumn colPrice;

    @FXML
    private TableColumn colQtyOnHand;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblTime;

    @FXML
    private TableView tblCart;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtOrderId;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXTextField txtUnitPrice;

    ObservableList<CartTM> cartTMS = FXCollections.observableArrayList();

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {

        String code = cmbItemCode.getValue().toString();
        String description = txtDescription.getText();
        Integer qtyOnHand= Integer.parseInt(txtQty.getText());
        Double unitPrice =  Double.parseDouble(txtUnitPrice.getText());
        Double total = qtyOnHand*unitPrice;

        cartTMS.add(new CartTM(code,description,qtyOnHand,unitPrice,total));
        tblCart.setItems(cartTMS);
        calNetTotal();



    }

    private void calNetTotal(){
        Double netTotal = 0.0;

        for (CartTM tm:cartTMS){
            netTotal+=tm.getTotal();
        }
        lblNetTotal.setText(netTotal.toString());
    }

    @FXML
    public void btnPlaceOrderOnAction(ActionEvent event) {
        String orderId = txtOrderId.getText();
        String date = lblDate.getText();
        String customerId = cmbCustomerId.getValue().toString();

        List<OrderDetail> orderDetails = new ArrayList<>();

        cartTMS.forEach(cartTM -> {
               orderDetails.add(
                       new OrderDetail(
                               orderId,
                               cartTM.getItemCode(),
                               cartTM.getQtyOnHand(),
                               cartTM.getUnitPrice()
                       )
               );
        });

        Order order = new Order(orderId, date, customerId, orderDetails);

        try {
            if (new OrderController().placeOrder(order)){
                new Alert(Alert.AlertType.INFORMATION,"Order Placed").show();
            }else{
                new Alert(Alert.AlertType.WARNING,"Order Not Placed").show();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDesciption.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("total"));

        
        setDateAndTime();
        loadCustomerIds();
        loadItemDoes();
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            
            if (newValue != null){
                searchCustomerData( newValue.toString());
            }
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue!=null){
                searchItemData(newValue.toString());
            }
        } );
    }

    private void loadItemDoes() {
        cmbItemCode.setItems(new ItemController().getItemCode());
    }

    private void searchItemData(String code) {
        Item item = new ItemController().searchItem(code);
        txtStock.setText(item.getCode().toString());
        txtUnitPrice.setText(item.getUnitPrice().toString());
        txtDescription.setText(item.getDescription());
    }

    private void searchCustomerData(String customerId) {
        Customer customer = new CustomerController().searchCustomer(customerId);
        txtCustomerName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
    }

    private void setDateAndTime(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(date);
        lblDate.setText(format);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,e->{
                    LocalTime now = LocalTime.now();
                    lblTime.setText(now.getHour()+":"+now.getMinute()+":"+now.getSecond());
                }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadCustomerIds(){
        cmbCustomerId.setItems(new CustomerController().getCustomerIds());
    }

    public void btnCommitOnAction(ActionEvent actionEvent) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.commit();
    }
}
