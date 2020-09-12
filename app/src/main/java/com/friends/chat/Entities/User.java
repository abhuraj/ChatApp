package com.friends.chat.Entities;

public class User {

    String userName;
    String password;

    public String getUserName() {
        return (userName == null) ? "" : userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return (password == null) ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
