package com.example.bookslending.po;

public class UsersInfo {
    //    public int _id;
    public int uId;
    public String uPass;
    public String uName;

    public UsersInfo() {

    }

    public UsersInfo(int uId) {
        this.uId = uId;
    }

    public UsersInfo(int uId, String uPass) {
        this.uId = uId;
        this.uPass = uPass;
    }

}

