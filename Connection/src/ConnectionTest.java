import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Auther: Carl
 * @Date: 2021/03/23/21:28
 * @Description:
 */
public class ConnectionTest {

    //方式一
    @Test
    public void test1() throws SQLException {
        //获取Driver的实现类对象
        Driver driver = new com.mysql.jdbc.Driver();

        //url: jdbc:mysql://localhost:3306/test
        //jdbc: mysql协议
        //localhost: ip地址
        //3306: 默认MySQL的端口号
        //test: 数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //将用户名和密码封装进Properties中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "hl828914");

        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }

    //方式二：对方式一的迭代：在如下的程序中不出现第三方API，使得程序具有更好的可移植性
    @Test
    public void test2() throws Exception {
        //利用反射实现：获取Driver的实现类对象
        Class aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        //提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //提供要连接的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "hl828914");

        //获取连接
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式三：使用DriverManager替换Driver
    @Test
    public void test3() throws Exception {
        //获取Driver的实现类对象
        Class aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        //提供连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "hl828914";

        //注册驱动
        DriverManager.registerDriver(driver);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //方式四：只是加载驱动，不需要显式的注册驱动
    @Test
    public void test4() throws Exception {
        //提供连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "hl828914";

        //加载Driver
        Class.forName("com.mysql.jdbc.Driver");
//        Driver driver = (Driver) aClass.newInstance();
//        //相较于方式三，如下的注册可以省略
//        DriverManager.registerDriver(driver);
        /**
         * 在mysql的Driver实现类中实现了如下操作
         *     static {
         *         try {
         *             DriverManager.registerDriver(new Driver());
         *         } catch (SQLException var1) {
         *             throw new RuntimeException("Can't register driver!");
         *         }
         *     }
         */

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //方式五：将数据库连接需要的4个基本信息声明在配置文件中，通过读取配置文件的方式获取连接
    //此种方式的优点是：
    //1. 实现了数据与代码的分离；实现了解耦
    //2. 如果需要修改配置文件信息，可以避免程序重新打包
    @Test
    public void test5() throws Exception {
        //读取配置文件的4个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //加载驱动
        Class.forName(driverClass);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }
}
