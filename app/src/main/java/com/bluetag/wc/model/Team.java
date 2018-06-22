
package com.bluetag.wc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Team {

    @SerializedName("team_id")
    @Expose
    private String teamId;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("team_name")
    @Expose
    private String teamName;
    @SerializedName("team_flag")
    @Expose
    private String teamFlag;
    @SerializedName("mplayed")
    @Expose
    private String mplayed;
    @SerializedName("won")
    @Expose
    private String won;
    @SerializedName("draw")
    @Expose
    private String draw;
    @SerializedName("lost")
    @Expose
    private String lost;
    @SerializedName("goalsfor")
    @Expose
    private String goalsfor;
    @SerializedName("goalsagainst")
    @Expose
    private String goalsagainst;
    @SerializedName("points")
    @Expose
    private String points;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamFlag() {
        return teamFlag;
    }

    public void setTeamFlag(String teamFlag) {
        this.teamFlag = teamFlag;
    }

    public String getMplayed() {
        return mplayed;
    }

    public void setMplayed(String mplayed) {
        this.mplayed = mplayed;
    }

    public String getWon() {
        return won;
    }

    public void setWon(String won) {
        this.won = won;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getLost() {
        return lost;
    }

    public void setLost(String lost) {
        this.lost = lost;
    }

    public String getGoalsfor() {
        return goalsfor;
    }

    public void setGoalsfor(String goalsfor) {
        this.goalsfor = goalsfor;
    }

    public String getGoalsagainst() {
        return goalsagainst;
    }

    public void setGoalsagainst(String goalsagainst) {
        this.goalsagainst = goalsagainst;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}
