package com.bluetag.wc.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ravi Tamada on 07/10/16.
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class LiveMatchModel {

    public String live_match;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public LiveMatchModel() {
    }

    public LiveMatchModel(String live_match) {
        this.live_match = live_match;
    }
}