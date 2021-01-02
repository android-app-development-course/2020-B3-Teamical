package com.xuexiang.temical.adapter.entity;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 管理的团队团队
 * 2021.1.2
 */
public class TeamJoin implements Cloneable {

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



    public TeamJoin() {

    }

    public TeamJoin(String teamName, String imageUrl) {
        TeamName = teamName;
        ImageUrl = imageUrl;
    }


    public TeamJoin(String teamName) {
        TeamName = teamName;
    }



    public TeamJoin setID(long ID) {
        this.ID = ID;
        return this;
    }

    public long getID() {
        return ID;
    }

    public TeamJoin setTeamName(String teamName) {
        TeamName = teamName;
        return this;
    }

    public String getTeamName() {
        return TeamName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public TeamJoin setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "TeamJoin{" +
                "TeamName='" + TeamName + '\'' +
                ", ImageUrl='" + ImageUrl + '\'' +
                '}';
    }

    @NonNull
    @Override
    public TeamJoin clone() {
        try {
            return (TeamJoin) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new TeamJoin();
    }
}
