package com.carl6.junit;

import com.carl1.util.JDBCUtils;
import com.carl4.bean.StuInfo;
import com.carl5.dao.StuInfoDAOImpl;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Auther: Carl
 * @Date: 2021/03/26/12:04
 * @Description:
 */
public class StuInfoDAOImplTest {

    private  StuInfoDAOImpl dao = new StuInfoDAOImpl();

    @Test
    public void insert() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            StuInfo stuInfo = new StuInfo(0, "王五", "男", 17, 02);
            dao.insert(connection,stuInfo);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void deleteById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            dao.deleteById(connection, 0);
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void update() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            StuInfo stuInfo = new StuInfo(4, "赵四", "男", 45, 01);
            dao.update(connection, stuInfo);
            System.out.println("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void getstuInfoById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            StuInfo stuInfo = dao.getstuInfoById(connection, 2);
            System.out.println("查询完成");
            System.out.println(stuInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void getAll() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            List<StuInfo> all = dao.getAll(connection);
            all.forEach(System.out::println);
            System.out.println(" ");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void getCount() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Long count = dao.getCount(connection);
            System.out.printf("查询完成，共有%d条记录\n", count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void getMaxAge() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            int maxAge = dao.getMaxAge(connection);
            System.out.printf("最大年龄为%d。", maxAge);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }
}