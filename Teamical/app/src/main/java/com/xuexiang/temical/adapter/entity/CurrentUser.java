package com.xuexiang.temical.adapter.entity;

public class CurrentUser{

    private static String UserName = "";

    private static String PhoneNum = "";

    private static String Password = "";

    public static String getUserName(){
        return UserName;
    }

    public static String getPhoneNum(){
        return PhoneNum;
    }

    public static String getPassword(){
        return Password;
    }

    public static void  setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public static void setPassword(String password) {
        Password = password;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }
}
