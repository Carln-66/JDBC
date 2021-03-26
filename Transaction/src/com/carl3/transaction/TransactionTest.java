package com.carl3.transaction;


import com.carl1.util.JDBCUtils;
import com.carl4.bean.UserTable;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @Auther: Carl
 * @Date: 2021/03/25/22:49
 * @Description: 事务测试
 * 事务：一组逻辑操作单元，使数据从一种状态变换到另外一个状态
 * >一组逻辑操作单元，一个或多个DML操作
 * <p>
 * 事务处理的原则：保证所有事物都做
 */
public class TransactionTest {

    /*
    针对于user_table
    update user_table set balance = balance - 500 where user = 'AA';
    update user_table set balance = balance + 500 where user = 'BB';
     */

    //******************************************未考虑事务******************************************************

    @Test
    public void testTransaction() {
        String sql1 = "update user_table set balance = balance - 500 where user = ?";
        update(sql1, "AA");

        //模拟网络异常
        System.out.println(10 / 0);

        String sql2 = "update user_table set balance = balance + 500 where user = ?";
        update(sql2, "BB");

        System.out.println("转账成功");
    }

    //未考虑数据库事务下的转账操作
    public int update(String sql, Object... args) { //sql中占位符的个数应该和可变形参的长度相同
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
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, ps);
        }
        return 0;
    }

    //******************************************考虑事务******************************************************


    @Test
    public void testUpdate() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            //取消数据自动提交
            connection.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 500 where user = ?";
            updateTransaction(connection, sql1, "AA");

            //模拟网络异常
            System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 500 where user = ?";
            updateTransaction(connection, sql2, "BB");

            System.out.println("转账成功");

            //提交
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            //回滚数据
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }


    //考虑数据库事务后的转账操作
    public int updateTransaction(Connection connection, String sql, Object... args) { //sql中占位符的个数应该和可变形参的长度相同
        PreparedStatement ps = null;
        try {
            //预编译sql语句，返回PreparedStatement的实例
            ps = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    //******************************************考虑隔离级别******************************************************

    @Test
    public void testTransactionSelect() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        //查询事务的隔离级别
        System.out.println(connection.getTransactionIsolation());

        //取消自动提交数据
        connection.setAutoCommit(false);

        String sql = "select user, password, balance from user_table where user = ?";
        UserTable cc = getInstance(connection, UserTable.class, sql, "CC");
        System.out.println(cc);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        //取消自动提交数据
        connection.setAutoCommit(false);

        //设置数据库的隔离级别
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        String sql = "update user_table set balance = ? where user = ?";
        updateTransaction(connection, sql, 4000, "CC");

        Thread.sleep(15000);
        System.out.println("修改结束");
    }

    /**
     * @Author: Carl
     * @Date: 2021/3/24 17:36
     * @param: [aClass, sql, args]
     * @return: T
     * @Description: 针对于不同表的通用查询操作，返回表中的一条记录(考虑事务)
     */
    public <T> T getInstance(Connection connection, Class<T> aClass, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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
            com.carl3.util.JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
