package com.carl3.util;

import com.carl4.bean.Demo;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;

/**
 * @Auther: Carl
 * @Date: 2021/03/24/12:46
 * @Description: 操作数据库工具类
 */
public class JDBCUtils {

    @Test
    public void test() {
        //测试
        String sql = "select id, stuname, seat from demo where id = ?";
        Demo data1 = queryForDemo(sql, 3);
        System.out.println(data1);

        String sql1= "select id, stuname from demo where id = ?";
        Demo data2 = queryForDemo(sql1, 4);
        System.out.println(data2);
    }

    /**
     * @Author: Carl
     * @Date: 2021/3/24 14:16
     * @param: [sql, args]
     * @return: com.carl4.bean.Demo
     * @Description: 针对于demo表的通用查询操作
     */
    public Demo queryForDemo(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            ResultSet rs = ps.executeQuery();

            //搜集结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                Demo data = new Demo();

                //处理一行数据集中的每个列
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);

                    //给data对象指定的columnName属性赋值为columnValue  ---  通过反射
                    Field field = Demo.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(data, columnValue);
                }
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
        return null;
    }


    /**
     * @Author: Carl
     * @Date: 2021/3/24 12:49
     * @param: []
     * @return: java.sql.Connection
     * @Description: 获取数据库连接
     */
    public static Connection getConnection() throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        //读取配置文件的4个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //加载驱动
        Class.forName(driverClass);

        //获取连接
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    /**
     * @Author: Carl
     * @Date: 2021/3/24 12:53
     * @param: [connection, ps]
     * @return: void
     * @Description: 关闭和连接Statement的操作
     */
    public static void closeResource(Connection connection, Statement ps) {
        //关闭资源 {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            if (ps != null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void closeResource(Connection connection, Statement ps, ResultSet rs) {
        //关闭资源 {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            if (ps != null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
