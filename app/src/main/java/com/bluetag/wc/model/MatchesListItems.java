package com.bluetag.wc.model;

/**
 * Created by Dream Land on 1/12/2018.
 */

public class MatchesListItems {

    private String id;
    private String date;
    private String round;
    private String team1;
    private String team2;
    private String score;
    private String details;
    //v2
    private String status;

    public MatchesListItems(String id, String date, String round, String team1, String team2, String score, String details, String status) {
        this.id = id;
        this.date = date;
        this.round = round;
        this.team1 = team1;
        this.team2 = team2;
        this.score = score;
        this.details = details;
        //v2
        this.status = status;

    }

    public MatchesListItems(String date, String round, String team1, String team2, String score) {
        this.date = date;
        this.round = round;
        this.team1 = team1;
        this.team2 = team2;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getRound() {
        return round;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getScore() {
        return score;
    }

    public String getDetails() {
        return details;
    }

    //v2

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
