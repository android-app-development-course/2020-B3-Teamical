package com.xuexiang.temical.adapter.entity;

import cn.bmob.v3.BmobObject;

public class User extends BmobObject {

    // 用户名
    private String UserName;
    // 手机号码 (唯一)
    private String PhoneNum;
    // 密码
    private String Password;

    public User(String userName, String phoneNum){
        this.UserName = userName;
        this.PhoneNum = phoneNum;
    }

    public User(){

    }

    public String getUserName(){ return UserName;}

    public String getPhoneNum(){
        return PhoneNum;
    }

    public User setUserName(String userName){
        this.UserName = userName;
        return this;
    }

    public User setPhoneNum(String phoneNum){
        this.PhoneNum = phoneNum;
        return this;
    }

    public String getPassword() {
        return Password;
    }

    public User setPassword(String password) {
        this.Password = password;
        return this;
    }
}
