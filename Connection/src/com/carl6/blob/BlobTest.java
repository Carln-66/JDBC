package com.carl6.blob;

import com.carl3.util.JDBCUtils;
import com.carl4.bean.StuInfo;
import org.junit.Test;

import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Auther: Carl
 * @Date: 2021/03/25/20:12
 * @Description: 测试使用PreparedStatement操作Blob类型的数据
 */
public class BlobTest {

    //向数据库中添加blob类型的数据
    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into stuinfo(id, name, gender, age, majorid, img) values(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setObject(1, 6);
        ps.setObject(2, "李四");
        ps.setObject(3, "男");
        ps.setObject(4, 22);
        ps.setObject(5, 07);

        //获取一个输入流
        FileInputStream fis = new FileInputStream(new File("壁纸1.jpg"));
        ps.setBlob(6, fis);

        ps.execute();

        JDBCUtils.closeResource(connection, ps);
    }

    //查询
    @Test
    public void query(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id, name, gender, age, majorid, img from stuinfo where id = ?";
            ps = connection.prepareStatement(sql);

            ps.setInt(1, 5);

            rs = ps.executeQuery();
            if (rs.next()) {
    //            int id = rs.getInt(1);
    //            String name = rs.getString(2);
    //            int age = rs.getInt(3);
    //            int majorid = rs.getInt(4);

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                int age = rs.getInt("age");
                int majorid = rs.getInt("majorid");

                StuInfo stuInfo = new StuInfo(id, name, gender, age, majorid);
                System.out.println(stuInfo);

                //将blob类型的字段下载下来，以文件的方式保存在本地
                Blob img = rs.getBlob("img");
                is = img.getBinaryStream();
                fos = new FileOutputStream("img.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1 ) {
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            JDBCUtils.closeResource(connection, ps, rs);
        }
    }
}
