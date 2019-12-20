package com.example.adminapp.Model;

public class User {
    private String username;
    private String password;
    private String phone;
    private String avatar;
    private int ckAccount;

    public int getCkAccount() {
        return ckAccount;
    }

    public void setCkAccount(int ckAccount) {
        this.ckAccount = ckAccount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public User(){}

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


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        //this.phone = phone;
    }
    public User(String phone, String password) {
        this.phone = phone;
        this.password = password;
        //this.phone = phone;
    }




}
