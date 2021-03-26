package com.carl1.connection;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Auther: Carl
 * @Date: 2021/03/26/13:56
 * @Description: 数据库连接池C3P0测试
 */
public class C2P0Test {

    //方式一：硬编码
    @Test
    public void testGetConnection() throws Exception {

        //获取c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("hl828914");

        //通过设置相关的参数对数据库连接池进行管理

        //设置数据库连接池中的初始连接数
        cpds.setInitialPoolSize(10);

        //获取连接
        Connection connection = cpds.getConnection();
        System.out.println(connection);

        //销毁c3p0连接池
        DataSources.destroy(cpds);
    }

    //方式二：使用配置文件
    @Test
    public void testGetConnection1() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
        Connection connection = cpds.getConnection();
        System.out.println(connection);
    }
}
