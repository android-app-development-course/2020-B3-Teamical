package com.xuexiang.temical.adapter.entity;


import cn.bmob.v3.BmobObject;

/**
 * 管理的团队团队
 * 2021.1.2
 */
public class Teammate extends BmobObject{

     // 团队名
    private String TeamName;
    // 团队负责人手机号
    private String ManagerPN;
    // 团队成员手机号
    private String MatePN;
    // 团队成员名字
    private String MateName;

    public Teammate() {

    }

    public Teammate(String teamName, String managerPN, String matePN, String mateName) {
        this.TeamName = teamName;
        this.ManagerPN = managerPN;
        this.MatePN = matePN;
        this.MateName = mateName;
    }

    public Teammate setTeamName(String teamName) {
        this.TeamName = teamName;
        return this;
    }

    public String getTeamName() {
        return TeamName;
    }

    public String getManagerPN() {
        return ManagerPN;
    }

    public Teammate setManagerPN(String managerPN) {
        this.ManagerPN = managerPN;
        return this;
    }

    public String getMateName() {
        return MateName;
    }

    public String getMatePN() {
        return MatePN;
    }

    public Teammate setMateName(String mateName) {
        this.MateName = mateName;
        return this;
    }

    public Teammate setMatePN(String matePN) {
        this.MatePN = matePN;
        return this;
    }
}
