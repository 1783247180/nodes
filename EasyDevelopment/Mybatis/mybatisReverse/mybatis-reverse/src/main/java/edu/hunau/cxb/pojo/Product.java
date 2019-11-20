package edu.hunau.cxb.pojo;

public class Product {
    private Integer pid;

    private String pname;

    private String ppassword1;

    private String ppassword2;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname == null ? null : pname.trim();
    }

    public String getPpassword1() {
        return ppassword1;
    }

    public void setPpassword1(String ppassword1) {
        this.ppassword1 = ppassword1 == null ? null : ppassword1.trim();
    }

    public String getPpassword2() {
        return ppassword2;
    }

    public void setPpassword2(String ppassword2) {
        this.ppassword2 = ppassword2 == null ? null : ppassword2.trim();
    }
}