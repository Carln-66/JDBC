package com.carl7.dao;

import com.carl1.util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Carl
 * @Date: 2021/03/26/9:14
 * @Description: 封装了针对数据表的通用操作
 */
public abstract class DAO<T> {

    private Class<T> aClass = null;

    {
        //获取当前DAO的子类继承的父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//获取了父类的泛型参数
        aClass = (Class<T>) actualTypeArguments[0]; //泛型的第一个参数
    }

    /**
    * @Author: Carl
    * @Date: 2021/3/26 9:27
    * @param: [connection, sql, args]
    * @return: int
    * @Description: 考虑数据库事务后的增删改操作
    */
    public int update(Connection connection, String sql, Object... args) { //sql中占位符的个数应该和可变形参的长度相同
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

    /**
     * @Author: Carl
     * @Date: 2021/3/24 17:36
     * @param: [aClass, sql, args]
     * @return: T
     * @Description: 针对于不同表的通用查询操作，返回表中的一条记录(考虑事务)
     */
    public  T getInstance(Connection connection,  String sql, Object... args) {
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

    /**
     * @Author: Carl
     * @Date: 2021/3/26 9:17
     * @param: [aClass, sql, args]
     * @return: java.util.List<T>
     * @Description: 返回多个对象的查询操作
     */
    public List<T> getForList(Connection connection, String sql, Object... args) {
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

            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = aClass.newInstance();

                //给T对象指定的属性赋值
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
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            com.carl3.util.JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    /**
    * @Author: Carl
    * @Date: 2021/3/26 9:24
    * @param: [connection, sql, args]
    * @return: E
    * @Description: 用于查询特殊值的通用方法
    */
    public <E> E getValue(Connection connection, String sql, Object... args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();

            if (rs.next()) {
                return (E) rs.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
