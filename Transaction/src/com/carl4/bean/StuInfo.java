package com.carl4.bean;

/**
 * @Auther: Carl
 * @Date: 2021/03/25/20:41
 * @Description:
 */
public class StuInfo {
    private int id;
    private String name;
    private String gender;
    private int age;
    private  int majorid;

    public StuInfo() {
    }

    public StuInfo(int id, String name, String gender, int age, int majorid) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.majorid = majorid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMajorid() {
        return majorid;
    }

    public void setMajorid(int majorid) {
        this.majorid = majorid;
    }

    @Override
    public String toString() {
        return "StuInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", majorid=" + majorid +
                '}';
    }
}
