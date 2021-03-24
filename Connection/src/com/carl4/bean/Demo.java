package com.carl4.bean;

/**
 * @Auther: Carl
 * @Date: 2021/03/24/13:42
 * @Description:
 *  ORM(Object Relational Mapping)变成思想
 *  一个数据表对应一个Java类
 *  表中的一条记录对应Java类的一个对象
 *  表中的一个字段对应Java类的一个属性
 */
public class Demo {
    private int id;
    private String stuname;
    private int seat;

    public Demo() {
    }

    public Demo(int id, String name, int seat) {
        this.id = id;
        this.stuname = name;
        this.seat = seat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return stuname;
    }

    public void setName(String name) {
        this.stuname = name;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", name='" + stuname + '\'' +
                ", seat=" + seat +
                '}';
    }
}
