package controller.order;

import db.DBConnection;
import model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class orderDetailController {

    public boolean addOrderDetail(List<OrderDetail> orderDetails){
        for (OrderDetail orderDetail:orderDetails){
             boolean isAddOrderDetail = addOrderDetail(orderDetail);

             if (!isAddOrderDetail){
                 return false;

             }
        }

        return true;

    }

    public boolean addOrderDetail(OrderDetail orderDetail){
        String SQl = "INSERT INTO orderdetail VALUES(?,?,?,?)";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQl);
            psTm.setObject(1,orderDetail.getOrderId());
            psTm.setObject(2,orderDetail.getItemCode());
            psTm.setObject(3,orderDetail.getQty());
            psTm.setObject(4,orderDetail.getUnitPrice());
            return psTm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
