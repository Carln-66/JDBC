package com.carl2.connectiontest;

import com.carl1.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

/**
 * @Auther: Carl
 * @Date: 2021/03/25/22:43
 * @Description:
 */
public class ConnectionTest {
    @Test
    public void testConnection() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        System.out.println(connection);
    }
}
