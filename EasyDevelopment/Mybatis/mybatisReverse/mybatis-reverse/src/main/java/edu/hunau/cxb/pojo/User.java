package edu.hunau.cxb.pojo;

public class User {
    private Integer userid;

    private String username;

    private String userpassword1;

    private Integer usermoney;

    private String userpassword2;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUserpassword1() {
        return userpassword1;
    }

    public void setUserpassword1(String userpassword1) {
        this.userpassword1 = userpassword1 == null ? null : userpassword1.trim();
    }

    public Integer getUsermoney() {
        return usermoney;
    }

    public void setUsermoney(Integer usermoney) {
        this.usermoney = usermoney;
    }

    public String getUserpassword2() {
        return userpassword2;
    }

    public void setUserpassword2(String userpassword2) {
        this.userpassword2 = userpassword2 == null ? null : userpassword2.trim();
    }
}