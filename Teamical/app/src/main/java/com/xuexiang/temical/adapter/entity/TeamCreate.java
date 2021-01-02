package com.xuexiang.temical.adapter.entity;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 管理的团队团队
 * 2021.1.2
 */
public class TeamCreate implements Cloneable {

    private static AtomicLong sAtomicLong = new AtomicLong();


    private long ID;

    /**
     * 头像图片
     */
    private String ImageUrl;

    /**
     * 团队名
     */
    private String TeamName;



    public TeamCreate() {

    }

    public TeamCreate(String teamName, String imageUrl) {
        TeamName = teamName;
        ImageUrl = imageUrl;
    }


    public TeamCreate(String teamName) {
        TeamName = teamName;
    }



    public TeamCreate setID(long ID) {
        this.ID = ID;
        return this;
    }

    public long getID() {
        return ID;
    }

    public TeamCreate setTeamName(String teamName) {
        TeamName = teamName;
        return this;
    }

    public String getTeamName() {
        return TeamName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public TeamCreate setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "NewInfo{" +
                "UserName='" + TeamName + '\'' +
                ", ImageUrl='" + ImageUrl + '\'' +
                '}';
    }

    @NonNull
    @Override
    public NewInfo clone() {
        try {
            return (NewInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new NewInfo();
    }
}
