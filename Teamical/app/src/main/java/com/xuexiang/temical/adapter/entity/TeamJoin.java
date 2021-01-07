package com.xuexiang.temical.adapter.entity;


import cn.bmob.v3.BmobObject;

/**
 * 加入的团队
 * 2021.1.2
 */
public class TeamJoin extends BmobObject{


    //  头像图片
    private String ImageUrl;

    // 团队名
    private String TeamName;
    //团队负责人手机号
    private String ManagerPN;

    public TeamJoin() {

    }

    public TeamJoin(String teamName) {
        TeamName = teamName;
    }

    public TeamJoin setTeamName(String teamName) {
        this.TeamName = teamName;
        return this;
    }

    public String getTeamName() {
        return TeamName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public TeamJoin setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
        return this;
    }

    public String getManagerPN() {
        return ManagerPN;
    }

    public TeamJoin setManagerPN(String managerPN) {
        this.ManagerPN = managerPN;
        return this;
    }
}
