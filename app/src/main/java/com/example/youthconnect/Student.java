package com.example.youthconnect;

public class Student {

    String name, sName, password, admin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Student(String admin, String name, String password, String sName) {
        this.name = name;
        this.sName = sName;
        this.password = password;
        this.admin = admin;
    }


}
