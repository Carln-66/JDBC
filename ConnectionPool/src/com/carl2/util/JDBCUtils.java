package com.carl2.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
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
}
