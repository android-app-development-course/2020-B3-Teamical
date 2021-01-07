package com.xuexiang.temical.adapter.entity;


import cn.bmob.v3.BmobObject;

/**
 * 管理的团队团队
 * 2021.1.2
 */
public class Notification extends BmobObject {

    private long ID;

    // 头像图片
    private String ImageUrl;

    // 申请用户
    private String UserName;

     // 团队名
    private String TeamName;

    private String Status;

    // 申请者手机号码
    private String ApplicantPN;

    // 审核者手机号码
    private String CheckerPN;


    public Notification() {

    }

    public Notification(String userName, String teamName, String status) {
        UserName = userName;
        TeamName = teamName;
        Status = status;
    }

    public Notification setID(long ID) {
        this.ID = ID;
        return this;
    }

    public long getID() {
        return ID;
    }

    public Notification setUserName(String userName) {
        UserName = userName;
        return this;
    }

    public String getUserName() {
        return UserName;
    }

    public Notification setTeamName(String teamName) {
        TeamName = teamName;
        return this;
    }

    public String getTeamName() {
        return TeamName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public Notification setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
        return this;
    }

    public String getStatus(){ return Status;}

    public void setStatus(String status) {Status = status;}

    public String getApplicantPN() {
        return ApplicantPN;
    }

    public String getCheckerPN() {
        return CheckerPN;
    }

    public Notification setApplicantPN(String applicantPN) {
        this.ApplicantPN = applicantPN;
        return this;
    }

    public Notification setCheckerPN(String checkerPN) {
        this.CheckerPN = checkerPN;
        return this;
    }
}
