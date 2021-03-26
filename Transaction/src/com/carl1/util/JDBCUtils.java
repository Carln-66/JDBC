package com.carl1.util;

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
