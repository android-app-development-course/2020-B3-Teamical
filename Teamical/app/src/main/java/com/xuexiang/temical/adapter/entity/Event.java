package com.xuexiang.temical.adapter.entity;


import cn.bmob.v3.BmobObject;

/**
 * 管理的团队团队
 * 2021.1.2
 */
public class Event extends BmobObject{


    //  头像图片
    private String detail;

    //团队负责人手机号
    private String username;

    public Event() {

    }

    public Event(String detail, String username) {
        this.detail = detail;
        this.username = username;
    }


    public String getDetail() {
        return detail;
    }

    public Event setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Event setUsername(String username) {
        this.username = username;
        return this;
    }
}
