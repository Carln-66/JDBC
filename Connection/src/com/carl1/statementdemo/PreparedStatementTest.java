package com.carl1.statementdemo;

import com.carl3.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @Auther: Carl
 * @Date: 2021/03/24/17:51
 * @Description: 使用PreparedStatement替换Statement解决SQL注入问题
 *
 *  除了解决Statement拼串，sql注入问题之外，PreparedStatement还有其他的优点
 *  1. PreparedStatement操作Blob的数据，而Statement做不到
 *  2. PreparedStatement可以实现高效的批量操作
 */
public class PreparedStatementTest {

    @Test
    public void testLogin() {
        Scanner scan = new Scanner(System.in);

        System.out.print("用户名：");
        String user = scan.nextLine();
        System.out.print("密  码：");
        String password = scan.nextLine();

        // SELECT user,password FROM DemoForQuery WHERE user = '1' or ' AND password = '
        // ='1' or '1' = '1';
        String sql = "SELECT `user`, `password` FROM `user_table` WHERE `user` = ? and `password` = ?";
        User returnUser = getInstance(User.class, sql, user, password);
        if (returnUser != null) {
            System.out.println("登陆成功!");
        } else {
            System.out.println("用户名或密码错误！");
        }
    }


    /**
     * @Author: Carl
     * @Date: 2021/3/24 17:36
     * @param: [aClass, sql, args]
     * @return: T
     * @Description: 针对于不同表的通用查询操作，返回表中的一条记录
     */
    public <T> T getInstance(Class<T> aClass, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
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
            JDBCUtils.closeResource(connection, ps, rs);
        }
        return null;
    }
}
