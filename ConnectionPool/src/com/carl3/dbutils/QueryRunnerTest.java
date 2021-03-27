package com.carl3.dbutils;

import com.alibaba.druid.util.JdbcUtils;
import com.carl2.util.JDBCUtils;
import com.carl4.bean.StuInfo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Carl
 * @Date: 2021/03/27/19:42
 * @Description: commons-dbutils 是Apache组织提供的一个开源JDBC工具类库，封装了针对于数据库的增删改查操作
 */
public class QueryRunnerTest {

    //测试插入数据
    @Test
    public void testInsert() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "insert into stuinfo (id, name, gender, age, majorid) values (?,?,?,?,?)";
            runner.update(conn, sql, null, "奥利奥", "女", 18, 22);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    //测试查询
    /**
    * @Author: Carl
    * @Date: 2021/3/27 20:07
    * @param: []
    * @return: void
    * @Description: BeanHandler：是ResultHandler接口的实现类，用于封装表中的一条记录
    */
    @Test
    public void testQuery1() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();

            String sql = "select id, name, gender, age, majorid from stuinfo where id = ?";
            BeanHandler<StuInfo> handler = new BeanHandler<>(StuInfo.class);
            StuInfo query = runner.query(connection, sql, handler, 7);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Author: Carl
    * @Date: 2021/3/27 20:11
    * @param: []
    * @return: void
    * @Description: BeanListHandler：是ResultHandler接口的实现类，用于封装表中的多条记录
    */
    @Test
    public void testQuery2() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();

            String sql = "select id, name, gender, age, majorid from stuinfo where id < ?";
            BeanListHandler<StuInfo> handler = new BeanListHandler<>(StuInfo.class);
            List<StuInfo> list = runner.query(connection, sql, handler, 7);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Author: Carl
    * @Date: 2021/3/27 20:19
    * @param: []
    * @return: void
    * @Description: MapHandler：是ResultHandler接口的实现类，对应表中的一条记录，将字段及字段相应的值作为map中的key和value。
    *  将字段对应字段的值作为map中的key和value
    */
    @Test
    public void testQuery3() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();

            String sql = "select id, name, gender, age, majorid from stuinfo where id = ?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(connection, sql, handler, 7);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Author: Carl
    * @Date: 2021/3/27 20:24
    * @param: []
    * @return: void
    * @Description: MapListHandler：是ResultHandler接口的实现类，对应表中的多条记录，将字段及相应字段的值作为map中的key和value，将这些map添加到list中
    */
    @Test
    public void testQuery4() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();

            String sql = "select id, name, gender, age, majorid from stuinfo where id < ?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> list = runner.query(connection, sql, handler, 7);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Author: Carl
    * @Date: 2021/3/27 20:28
    * @param: []
    * @return: void
    * @Description: ScalarHandler：查询对象相关的字段
    */
    @Test
    public void testQuery5() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();

            String sql = "select count(*) from stuinfo";
            ScalarHandler handler = new ScalarHandler();
            long count = (long) runner.query(connection, sql, handler);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
     * @Author: Carl
     * @Date: 2021/3/27 20:28
     * @param: []
     * @return: void
     * @Description: ScalarHandler：查询特殊值
     */
    @Test
    public void testQuery6() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();

            String sql = "select max(age) from stuinfo";
            ScalarHandler handler = new ScalarHandler();
            int age = (int) runner.query(connection, sql, handler);
            System.out.println(age);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Author: Carl
    * @Date: 2021/3/27 20:34
    * @param: []
    * @return: void
    * @Description: 自定义ResultSetHandler的实现类
    */
    @Test
    public void testQuery7() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();

            String sql = "select id, name, gender, age, majorid from stuinfo where id = ?";
            ResultSetHandler<StuInfo> handler = new ResultSetHandler<StuInfo>(){

                @Override
                public StuInfo handle(ResultSet resultSet) throws SQLException {
                    return null;
                }
            };
            StuInfo stuInfo = runner.query(connection, sql, handler, 5);
            System.out.println(stuInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }
}
