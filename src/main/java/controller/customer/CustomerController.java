package controller.customer;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerController implements CustomerService {
    @Override
    public boolean addCustomer(Customer customer) {
       String SQL = "INSERT INTO customer VALUES(?,?,?,?)";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1,customer.getId());
            psTm.setObject(2,customer.getName());
            psTm.setObject(3,customer.getAddress());
            psTm.setObject(4,customer.getSalary());
            return psTm.executeUpdate()>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        return false;
    }

    @Override
    public Customer searchCustomer(String id) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM customer WHERE id = "+"'" + id + "' ");
            resultSet.next();
            return new Customer(
                   resultSet.getString(1),
                   resultSet.getString(2),
                   resultSet.getString(3),
                   resultSet.getDouble(4)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> getAll() {
        ArrayList<Customer> customerArrayList = new ArrayList<>();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `customer`");
            while (resultSet.next()){
                Customer customer = new Customer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4)
                );
                customerArrayList.add(customer);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerArrayList;
    }

    public ObservableList<String> getCustomerIds(){
        List<Customer> customerList = getAll();
        ObservableList<String> customerIdList = FXCollections.observableArrayList();

        customerList.forEach(customer -> {
            customerIdList.add(customer.getId());
        });

        return customerIdList;
    }

    @Override
    public boolean deleteCustomer(String id) {
        return false;
    }
}
