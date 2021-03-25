package com.carl6.blob;

import com.carl3.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * @Auther: Carl
 * @Date: 2021/03/25/21:33
 * @Description: 使用PreparedStatement实现批量数据的操作
 *  update、delete本身就具有批量操作的效果
 *  这里演示的批量操作主要面向批量插入(insert)，使用PreparedStatement实现更高效的批量插入
 *
 *  方式一：使用Statement
 *
 */
public class InsertTest {

    //方式一：使用Statement
//    @Test
//    public void testInsert(){
//        Connection connection = JDBCUtils.getConnection();
//        Statement st = connection.createStatement();
//        for (int i = 0; i < 20000; i++) {
//            String sql = "insert into goods(name), value ('name_" + i + "')";
//            st.execute(sql);
//        }
//    }

    //批量插入方式二：使用PreparedStatement
    @Test
    public void testInsert1(){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values (?)";
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("消耗时间："+  (end - start));
            //消耗时间：63351
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
    }

    //批量插入方式三：addBatch(), executeBatch(), clearBatch()
    @Test
    public void testInsert2(){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values (?)";
            ps = connection.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++) {
                ps.setObject(1, "name_" + i);

                //1. 攒sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //2. 执行Batch
                    ps.executeBatch();
                    //3. 清空Batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("消耗时间："+  (end - start));
            //消耗时间：1492
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
    }

    //批量插入方式四：
    @Test
    public void testInsert3(){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            connection = JDBCUtils.getConnection();

            //设置不允许自动提交数据
            connection.setAutoCommit(false);

            String sql = "insert into goods(name) values (?)";
            ps = connection.prepareStatement(sql);
            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, "name_" + i);

                //1. 攒sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //2. 执行Batch
                    ps.executeBatch();
                    //3. 清空Batch
                    ps.clearBatch();
                }
            }

            connection.commit();
            long end = System.currentTimeMillis();
            System.out.println("消耗时间："+  (end - start));
            //插入一百万条数据消耗时间：7578
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
    }

}
