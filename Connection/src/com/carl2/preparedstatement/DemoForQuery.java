package com.carl2.preparedstatement;

import com.carl3.util.JDBCUtils;
import com.carl4.bean.Demo;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Auther: Carl
 * @Date: 2021/03/24/13:25
 * @Description:    针对于表的查询操作
 */
public class DemoForQuery {

    @Test
    public void testQuery1() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();

            String sql = "select * from demo where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, 1);

            //执行，并返回结果集
            resultSet = ps.executeQuery();

            //处理结果集
            if (resultSet.next()) { //next(): 判断结果集的下一条是否有数据，如果有数据返回true，并下移，如果返回false，直接结束
                //获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int seat = resultSet.getInt(3);

                //方式一：直接打印信息
    //            System.out.println("id = " + name + ", seat number = " + seat);

                //方式二：利用数组显示
    //            Object[] demo = new Object[]{name, seat};

                //方式三：将其封装在一个对象中
                Demo demo = new Demo(id, name, seat);
                System.out.println(demo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, resultSet);
        }
    }
}
