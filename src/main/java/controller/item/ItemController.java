package controller.item;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemController implements ItemService{

    @Override
    public boolean addItem() {
        return false;
    }

    @Override
    public boolean updateItem() {
        return false;
    }

    @Override
    public boolean deleteItem() {
        return false;
    }

    @Override
    public Item searchItem(String code) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM item WHERE ItemCode =" + "'" + code + "' ");
            resultSet.next();
            return  new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Item> getAll() {
        try {
            ArrayList<Item> itemList = new ArrayList<>();
            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM item");

            while (resultSet.next()){
                itemList.add(
                        new Item(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getInt(4)
                        )
                );
            }

            return itemList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ObservableList<String> getItemCode(){
        ObservableList<String> itemCodeList = FXCollections.observableArrayList();
        List<Item> itemList = getAll();
        itemList.forEach(item -> {
            itemCodeList.add(item.getCode());
        });

        return itemCodeList;

    }

    public boolean updateStock(List<OrderDetail> orderDetails){
        for (OrderDetail orderDetail:orderDetails){
            boolean isUpdateStock = updateStock(orderDetail);
            if (!isUpdateStock){
                return false;
            }
        }

        return true;

    }

    public boolean  updateStock(OrderDetail orderDetail){
        String SQl = "UPDATE item SET stock = stock-? WHERE ItemCode = ?";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQl);
            psTm.setObject(1,orderDetail.getQty());
            psTm.setObject(2,orderDetail.getItemCode());
            return psTm.executeUpdate()>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
