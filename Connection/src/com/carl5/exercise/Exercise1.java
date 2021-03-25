package com.carl5.exercise;

import com.carl3.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @Auther: Carl
 * @Date: 2021/03/25/16:20
 * @Description:
 */
public class Exercise1 {

    @Test
    public void testInsert() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入编号");
        int id = scanner.nextInt();
        System.out.println("请输入学生姓名");
        String name = scanner.next();
        System.out.println("请输入性别");
        String gender = scanner.next();
        System.out.println("请输入年龄");
        int age = scanner.nextInt();
        System.out.println("请输入专业id");
        int majorid = scanner.nextInt();

        String sql = "insert into stuinfo values(?,?,?,?,?)";
        int updateNum = update(sql, id, name, gender, age, majorid);
        System.out.println(updateNum);

        if (updateNum > 0) {
            System.out.println("操作成功");
        } else {
            System.out.println("操作失败");
        }

    }

    public int update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //获取链接，预编译
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //如果执行的是查询操作，有返回值结果，则此方法ps.execute();返回true
            //如果执行的是增删改操作，没有返回值，那么此方法返回false
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
        return 0;
    }
}
