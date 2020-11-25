package com.xuexiang.temical.adapter.entity;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 新闻信息
 *
 * @author xuexiang
 * @since 2019/4/7 下午12:07
 */
public class TeammateInfo implements Cloneable {

    private static AtomicLong sAtomicLong = new AtomicLong();


    private long ID;

    /**
     * 头像图片
     */
    private String ImageUrl;

    /**
     * 用户名
     */
    private String UserName;



    public TeammateInfo() {

    }

    public TeammateInfo(String userName, String imageUrl) {
        UserName = userName;
        ImageUrl = imageUrl;
    }


    public TeammateInfo(String userName) {
        UserName = userName;
    }



    public TeammateInfo setID(long ID) {
        this.ID = ID;
        return this;
    }

    public long getID() {
        return ID;
    }

    public TeammateInfo setUserName(String userName) {
        UserName = userName;
        return this;
    }

    public String getUsername() {
        return UserName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public TeammateInfo setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "NewInfo{" +
                "UserName='" + UserName + '\'' +
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
