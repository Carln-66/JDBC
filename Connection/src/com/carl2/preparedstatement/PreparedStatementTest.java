package com.carl2.preparedstatement;

import com.carl3.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Auther: Carl
 * @Date: 2021/03/24/10:01
 * @Description: 使用PreparedStatement替换Statement，实现对数据表的增删改查操作
 * <p>
 * 增删改查
 */
public class PreparedStatementTest {

    //通用的增删改操作
    @Test
    public void testCommonUpdate(){
//        String sql = "delete from DemoForQuery where user = ?";
//        update(sql, "Carl");

        String sql = "insert into demo values (?, ?, ?)";
        update(sql, null, "Carl", 7);
    }


    public void update(String sql, Object... args) { //sql中占位符的个数应该和可变形参的长度相同
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //获取数据库的连接
            connection = JDBCUtils.getConnection();

            //预编译sql语句，返回PreparedStatement的实例
            ps = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, ps);
        }
    }

    //修改customer表的一条记录
    @Test
    public void testUpdate() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1. 获取数据库的连接
            connection = JDBCUtils.getConnection();

            //2. 预编译sql语句，返回PrepareStatement的实例
            String sql = "update user_table set user = ? where user = ?";
            ps = connection.prepareStatement(sql);

            //3. 填充占位符
            ps.setString(1, "SSS");
            ps.setString(2, "AA");

            //4. 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 资源的关闭
            JDBCUtils.closeResource(connection, ps);
        }
    }

    //向employee表中添加一条记录
    @Test
    public void test() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //读取配置文件的4个基本信息
            InputStream is = PreparedStatementTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
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

            //预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into user_table values (?, ?, ?)";   //?:占位符
            preparedStatement = connection.prepareStatement(sql);

            //填充占位符
            preparedStatement.setString(1, "Carl");
            preparedStatement.setInt(2, 123456);
            preparedStatement.setDouble(3, 3000);

            //执行操作
            preparedStatement.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //关闭资源 {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
