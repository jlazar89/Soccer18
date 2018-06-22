
package com.bluetag.wc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PastWinner implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("winner_name")
    @Expose
    private String winnerName;
    @SerializedName("winner_flag")
    @Expose
    private String winnerFlag;
    @SerializedName("runner_up_name")
    @Expose
    private String runnerUpName;
    @SerializedName("runner_up_flag")
    @Expose
    private String runnerUpFlag;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("host_country")
    @Expose
    private String hostCountry;
    @SerializedName("host_stadium")
    @Expose
    private String hostStadium;
    @SerializedName("golden_ball")
    @Expose
    private String goldenBall;
    @SerializedName("golden_boot")
    @Expose
    private String goldenBoot;
    @SerializedName("golden_glove")
    @Expose
    private String goldenGlove;
    @SerializedName("best_goalkeeper")
    @Expose
    private String bestGoalkeeper;
    @SerializedName("year")
    @Expose
    private String year;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerFlag() {
        return winnerFlag;
    }

    public void setWinnerFlag(String winnerFlag) {
        this.winnerFlag = winnerFlag;
    }

    public String getRunnerUpName() {
        return runnerUpName;
    }

    public void setRunnerUpName(String runnerUpName) {
        this.runnerUpName = runnerUpName;
    }

    public String getRunnerUpFlag() {
        return runnerUpFlag;
    }

    public void setRunnerUpFlag(String runnerUpFlag) {
        this.runnerUpFlag = runnerUpFlag;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHostCountry() {
        return hostCountry;
    }

    public void setHostCountry(String hostCountry) {
        this.hostCountry = hostCountry;
    }

    public String getHostStadium() {
        return hostStadium;
    }

    public void setHostStadium(String hostStadium) {
        this.hostStadium = hostStadium;
    }

    public String getGoldenBall() {
        return goldenBall;
    }

    public void setGoldenBall(String goldenBall) {
        this.goldenBall = goldenBall;
    }

    public String getGoldenBoot() {
        return goldenBoot;
    }

    public void setGoldenBoot(String goldenBoot) {
        this.goldenBoot = goldenBoot;
    }

    public String getGoldenGlove() {
        return goldenGlove;
    }

    public void setGoldenGlove(String goldenGlove) {
        this.goldenGlove = goldenGlove;
    }

    public String getBestGoalkeeper() {
        return bestGoalkeeper;
    }

    public void setBestGoalkeeper(String bestGoalkeeper) {
        this.bestGoalkeeper = bestGoalkeeper;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
