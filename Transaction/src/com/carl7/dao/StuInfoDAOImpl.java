package com.carl7.dao;

import com.carl4.bean.StuInfo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;

/**
 * @Auther: Carl
 * @Date: 2021/03/26/9:40
 * @Description: 封装针对于数据表的通用操作
 */
public class StuInfoDAOImpl extends DAO<StuInfo> implements StuInfoDAO {

    @Override
    public void insert(Connection connection, StuInfo stuInfo) {
        String sql = "insert into stuinfo(id, name, gender, age, majorid) values(?, ?, ?, ?, ?)";
        update(connection, sql, stuInfo.getId(), stuInfo.getName(), stuInfo.getGender(), stuInfo.getAge(), stuInfo.getMajorid());
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql = "delete from stuinfo where id = ?";
        update(connection, sql, id);
    }

    @Override
    public void update(Connection connection, StuInfo stuInfo) {
        String sql = "update stuinfo set name = ?, gender = ?, age = ?, majorid = ? where id = ?";
        update(connection, sql, stuInfo.getName(), stuInfo.getGender(), stuInfo.getAge(), stuInfo.getMajorid(), stuInfo.getId());
    }

    @Override
    public StuInfo getstuInfoById(Connection connection, int id) {
        String sql = "select id, name, gender, age, majorid from stuinfo where id = ?";
        StuInfo stu = getInstance(connection, sql, id);
        return stu;
    }

    @Override
    public List<StuInfo> getAll(Connection connection) {
        String sql = "select id, name, gender, age, majorid from stuinfo";
        List<StuInfo> list = getForList(connection, sql);
        return list;
    }

    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from stuinfo";
        return getValue(connection, sql);
    }

    @Override
    public int getMaxAge(Connection connection) {
        String sql = "select max(age) from stuinfo";
        return getValue(connection, sql);
    }
}
