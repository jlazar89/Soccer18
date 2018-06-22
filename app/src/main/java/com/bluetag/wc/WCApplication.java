package com.bluetag.wc;

import android.app.Application;

import com.bluetag.wc.jobs.DemoJobCreator;
import com.evernote.android.job.JobManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Jeffy on 5/27/2017.
 */

public class WCApplication extends Application {
    private static InterstitialAd mInterstitialAd;
    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new DemoJobCreator());
//        JobManager.instance().getConfig().setAllowSmallerIntervalsForMarshmallow(true);

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.browser_back_interstital_ad_unit_id));
        WCApplication.requestNewInterstitial();
    }

    public static void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("DBA681008FF649057E4250D6D754C0DD")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public static void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            requestNewInterstitial();
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            //startGame();
        }
    }
}
