package com.xuexiang.temical.adapter.entity;


import cn.bmob.v3.BmobObject;

/**
 * 管理的团队团队
 * 2021.1.2
 */
public class TeamCreate extends BmobObject{


    //  头像图片
    private String ImageUrl;

     // 团队名
    private String TeamName;
    //团队负责人手机号
    private String ManagerPN;

    public TeamCreate() {

    }

    public TeamCreate(String teamName, String imageUrl) {
        this.TeamName = teamName;
        this.ImageUrl = imageUrl;
    }

    public TeamCreate(String teamName) {
        TeamName = teamName;
    }

    public TeamCreate setTeamName(String teamName) {
        this.TeamName = teamName;
        return this;
    }

    public String getTeamName() {
        return TeamName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public TeamCreate setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
        return this;
    }

    public String getManagerPN() {
        return ManagerPN;
    }

    public TeamCreate setManagerPN(String managerPN) {
        this.ManagerPN = managerPN;
        return this;
    }
}
