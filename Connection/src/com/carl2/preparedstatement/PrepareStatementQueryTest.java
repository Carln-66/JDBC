package com.carl2.preparedstatement;

import com.carl3.util.JDBCUtils;
import com.carl4.bean.Demo;
import com.carl4.bean.Order;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Carl
 * @Date: 2021/03/24/17:20
 * @Description: 利用PrepareStatement语句实现不同表的通用查询操作
 */
public class PrepareStatementQueryTest {

    @Test
    public void testGetForList(){
        String sql = "select id, stuname, seat from demo where id < ?";
        List<Demo> list = getForList(Demo.class, sql, 2);
        list.forEach(System.out::println);

        String sql1 = "select order_id orderId, order_name orderName, order_date orderDate from `order`";
        List<Order> list1 = getForList(Order.class, sql1);
        list1.forEach(System.out::println);
    }


    public <T> List<T> getForList(Class<T> aClass, String sql, Object... args) {
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

            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = aClass.newInstance();

                //给T对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值，通过ResultSet
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名，通过ResultSetMetaData  --不推荐使用
//                    String columnName = rsmd.getColumnName(i + 1);

                    //获取列的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = aClass.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }
        return null;
    }

    @Test
    public void testGetInstance() {
        String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?";
        Order instance = getInstance(Order.class, sql, 5);
        System.out.println(instance);

        String sql1 = "select id, stuname, seat from demo where id = ?";
        Demo instance1 = getInstance(Demo.class, sql1, 2);
        System.out.println(instance1);
    }


    /**
     * @Author: Carl
     * @Date: 2021/3/24 17:36
     * @param: [aClass, sql, args]
     * @return: T
     * @Description: 针对于不同表的通用查询操作，返回表中的一条记录
     */
    public <T> T getInstance(Class<T> aClass, String sql, Object... args) {
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
                T t = aClass.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值，通过ResultSet
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名，通过ResultSetMetaData  --不推荐使用
//                    String columnName = rsmd.getColumnName(i + 1);

                    //获取列的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = aClass.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }
        return null;
    }
}
