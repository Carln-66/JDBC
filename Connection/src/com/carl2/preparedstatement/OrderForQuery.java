package com.carl2.preparedstatement;

import com.carl3.util.JDBCUtils;
import com.carl4.bean.Order;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @Auther: Carl
 * @Date: 2021/03/24/15:12
 * @Description:
 */
public class OrderForQuery {

    @Test
    public void testOrderForQuery(){
        String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?";

        Order order = orderForQuery(sql, 1);
        System.out.println(order);
    }


    /**
     * @Author: Carl
     * @Date: 2021/3/24 15:44
     * @param: [sql, args]
     * @return: com.carl4.bean.Order
     * @Description: 针对于oder表的通用查询操作
     *  * 针对于表的字段名与类的属性名不一致的情况：必须在声明sql的时候使用类的属性名来明明字段的别名
     *  * 使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName()获取列的别名
     *  说明：如果sql中没有给字段起别名，getColumnLabel()获取的就是列名
     *
     */
    public Order orderForQuery(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行，获取结果集
            rs = ps.executeQuery();

            //获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();

            //获取列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                Order order = new Order();

                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值，通过ResultSet
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名，通过ResultSetMetaData  --不推荐使用
//                    String columnName = rsmd.getColumnName(i + 1);

                    //获取列的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }

        return null;
    }

    @Test
    public void testQuery1() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();

            String sql = "select order_id, order_name, order_date from `order` where order_id = ?";
            ps = connection.prepareStatement(sql);

            ps.setObject(1, 1);

            rs = ps.executeQuery();

            if (rs.next()) {
                int id = (int) rs.getObject(1);
                String name = (String) rs.getObject(2);
                Date date = (Date) rs.getObject(3);

                Order order = new Order(id, name, date);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }
    }
}
