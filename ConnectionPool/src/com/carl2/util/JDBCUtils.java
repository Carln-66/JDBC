package com.carl2.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Auther: Carl
 * @Date: 2021/03/26/17:38
 * @Description:
 */
public class JDBCUtils {

    /**
    * @Author: Carl
    * @Date: 2021/3/26 17:39
    * @param: []
    * @return: java.sql.Connection
    * @Description: 使用c3p0的数据库连接池技术
    */
    //提供一个数据库连接池
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
    public static Connection getConnection1() throws SQLException {
        Connection connection = cpds.getConnection();
        return connection;
    }

    /**
    * @Author: Carl
    * @Date: 2021/3/26 18:20
    * @param: []
    * @return: java.sql.Connection
    * @Description: 使用dbcp数据库连接池技术获取数据库连接
    */
    private static  DataSource source;
    static {
        try {
            Properties pros = new Properties();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");

            pros.load(is);

            //核心代码：造数据库连接池
            DataSource dataSource = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection2() throws Exception {
        Connection connection = source.getConnection();
        return connection;
    }

    /**
    * @Author: Carl
    * @Date: 2021/3/27 19:36
    * @param:
    * @return:
    * @Description: 使用Druid数据库连接池进行连接
    */
    private static DataSource source1;
    static {
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);
            source1 = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection3 () throws Exception {
        Connection connection = source1.getConnection();
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

    public static void closeResource1(Connection conn, Statement ps, ResultSet rs) {
//        try {
//            DbUtils.close(conn);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(ps);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(rs);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(rs);
    }
}
