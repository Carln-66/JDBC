package com.carl1.connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * @Auther: Carl
 * @Date: 2021/03/27/19:12
 * @Description: Druid数据库连接池测试
 */
public class DruidTest {

    @Test
    public static void getConnection() throws Exception {
        Properties pros = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        pros.load(is);
        DataSource source = DruidDataSourceFactory.createDataSource(pros);

        Connection connection = source.getConnection();
        System.out.println(connection);
    }
}
