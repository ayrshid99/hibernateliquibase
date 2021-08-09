package com.example.hibernateddl;


import javax.persistence.*;

@Entity
public class Sample {
    @Id
    long id;
    @Column(name = "username")

    String username;
    @Column(name = "password")
    String password;


    String oneMoreField2;

    String oneMoreField3;

    public String getOneMoreField3() {
        return oneMoreField3;
    }

    public void setOneMoreField3(String oneMoreField3) {
        this.oneMoreField3 = oneMoreField3;
    }

    public String getOneMoreField2() {
        return oneMoreField2;
    }

    public void setOneMoreField2(String oneMoreField2) {
        this.oneMoreField2 = oneMoreField2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
