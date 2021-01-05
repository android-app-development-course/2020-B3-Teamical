package com.xuexiang.temical.adapter.entity;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 管理的团队团队
 * 2021.1.2
 */
public class Notification implements Cloneable {

    private long ID;

    /**
     * 头像图片
     */
    private String ImageUrl;

    /**
     * 申请用户
     */
    private String UserName;

    /**
     * 团队名
     */
    private String TeamName;

    private String Status;


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

    @NonNull
    @Override
    public String toString() {
        return "Notification{" +
                "UserName='" + UserName + '\'' +
                ", TeamName='" + TeamName + '\'' +
                '}';
    }

    @NonNull
    @Override
    public Notification clone() {
        try {
            return (Notification) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Notification();
    }
}