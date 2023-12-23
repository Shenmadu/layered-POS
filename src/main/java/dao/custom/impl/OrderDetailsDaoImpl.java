package dao.custom.impl;

import db.DBConnection;
import dto.OrderDetailsDto;
import dao.custom.OrderDetailsDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsDaoImpl implements OrderDetailsDao {
    @Override
    public boolean saveOrderDetails(List<OrderDetailsDto> list) throws SQLException, ClassNotFoundException {
       boolean isDetailsSaved=true;
        for (OrderDetailsDto dto:list) {
            String sql="INSERT INTO orderdetail VALUES(?,?,?,?)";
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setString(1,dto.getOrderId());
            pstm.setString(2,dto.getItemCode());
            pstm.setInt(3,dto.getQty());
            pstm.setDouble(4,dto.getUnitPrice());

            if(!(pstm.executeUpdate()>0)){
                isDetailsSaved=false;
            }
        }
        return isDetailsSaved;
    }

    @Override
    public List<OrderDetailsDto> getOrders(String id) throws SQLException, ClassNotFoundException {
        List<OrderDetailsDto> list=new ArrayList<>();
        String sql="SELECT * FROM orderdetail WHERE orderId =?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);


        pstm.setString(1, id);
        ResultSet result = pstm.executeQuery();

        while (result.next()){
            list.add(new OrderDetailsDto(
                    result.getString(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getDouble(4)
            ));
        }
        return list;
    }

}
