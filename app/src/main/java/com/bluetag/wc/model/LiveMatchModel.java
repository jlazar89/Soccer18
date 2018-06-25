package com.bluetag.wc.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Ravi Tamada on 07/10/16.
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class LiveMatchModel implements Serializable{

    public String live_match_1_url;
    public String match_1_details;

    public String live_match_2_url;
    public String match_2_details;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public LiveMatchModel() {
    }

    public String getLive_match_1_url() {
        return live_match_1_url;
    }

    public void setLive_match_1_url(String live_match_1_url) {
        this.live_match_1_url = live_match_1_url;
    }

    public String getMatch_1_details() {
        return match_1_details;
    }

    public void setMatch_1_details(String match_1_details) {
        this.match_1_details = match_1_details;
    }

    public String getLive_match_2_url() {
        return live_match_2_url;
    }

    public void setLive_match_2_url(String live_match_2_url) {
        this.live_match_2_url = live_match_2_url;
    }

    public String getMatch_2_details() {
        return match_2_details;
    }

    public void setMatch_2_details(String match_2_details) {
        this.match_2_details = match_2_details;
    }
}