package com.carl5.dao;

import com.carl4.bean.StuInfo;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: Carl
 * @Date: 2021/03/26/9:29
 * @Description: 此接口规范针对于stuinfo表的常用操作
 */
public interface StuInfoDAO {

    /**
    * @Author: Carl
    * @Date: 2021/3/26 9:31
    * @param: [connection, stuInfo]
    * @return: void
    * @Description: 将stuinfo对象添加到数据库中
    */
    void insert(Connection connection, StuInfo stuInfo);

    /**
    * @Author: Carl
    * @Date: 2021/3/26 9:32
    * @param: [connection, id]
    * @return: void
    * @Description: 根据指定ID删除表中一条记录
    */
    void deleteById(Connection connection, int id);

    /**
    * @Author: Carl
    * @Date: 2021/3/26 9:33
    * @param: [connection, id, stuInfo]
    * @return: void
    * @Description: 根据内存中的stuinfo对象更改用户记录信息
    */
    void update(Connection connection, StuInfo stuInfo);

    /**
    * @Author: Carl
    * @Date: 2021/3/26 9:34
    * @param: [connection, id]
    * @return: void
    * @Description: 针对指定的id查询信息
    */
    StuInfo getstuInfoById(Connection connection, int id);

    /**
    * @Author: Carl
    * @Date: 2021/3/26 9:36
    * @param: [connection]
    * @return: java.util.List<com.carl4.bean.StuInfo>
    * @Description: 查询表中所有记录构成的集合
    */
    List<StuInfo> getAll(Connection connection);

    /**
    * @Author: Carl
    * @Date: 2021/3/26 9:38
    * @param: [connection]
    * @return: java.lang.Long
    * @Description: 返回数据表中数据的数量
    */
    Long getCount(Connection connection);

    /**
    * @Author: Carl
    * @Date: 2021/3/26 9:39
    * @param: []
    * @return: int
    * @Description: 返回数据表中最大的年龄
    */
    int getMaxAge(Connection connection);
}
