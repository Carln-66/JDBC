package com.carl4.bean;

/**
 * @Auther: Carl
 * @Date: 2021/03/26/8:55
 * @Description:    user_table数据库的bean
 */
public class UserTable {
    private String user;
    private int password;
    private double balance;

    public UserTable(String user, int password, double balance) {
        this.user = user;
        this.password = password;
        this.balance = balance;
    }

    public UserTable() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "UserTable{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}
