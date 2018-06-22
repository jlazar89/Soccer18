package com.bluetag.wc.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.utils.Flags;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dream Land on 2/23/2018.
 */

public class MatchDetailsFragment extends Fragment {

    TextView tvDateTime, tvRound, tvTeam1, tvTeam2, tvScore, tvDetails;
    CircleImageView imgTeam1, imgTeam2;
    Bundle bundle;
    String dateTime, round, team1, team2, score, details;
    ActionBar actionBar;
    ActionBarDrawerToggle toggle;

    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.match_details_fragment, container, false);

        iniViewsAndValues(rootView);
        getValues();
        setValues();

        mAdView = (AdView)rootView. findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        return rootView;
    }

    public void setFunc(ActionBar actionBar, ActionBarDrawerToggle toggle) {
        this.actionBar = actionBar;
        this.toggle = toggle;
        this.toggle.setDrawerIndicatorEnabled(false);
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setHomeButtonEnabled(true);
    }

    private void iniViewsAndValues (View v) {
        tvDateTime = v.findViewById(R.id.tv_date_time);
        tvRound = v.findViewById(R.id.tv_round);
        tvTeam1 = v.findViewById(R.id.TextViewTeam1MatchDetails);
        tvTeam2 = v.findViewById(R.id.TextViewTeam2MatchDetails);
        tvScore = v.findViewById(R.id.TextViewScoreMatchDetails);
        tvDetails = v.findViewById(R.id.TextViewMatchDetails);
        imgTeam1 = v.findViewById(R.id.ImageTeam1MatchDetails);
        imgTeam2 = v.findViewById(R.id.ImageTeam2MatchDetails);
        dateTime = null;
        round = null;
        team1 = null;
        team2 = null;
        score = null;
        details = null;
    }

    private void getValues() {
        bundle = getArguments();
        if (bundle != null) {
            dateTime = bundle.getString("DATE_TIME");
            round = bundle.getString("ROUND");
            team1 = bundle.getString("TEAM1");
            team2 = bundle.getString("TEAM2");
            score = bundle.getString("SCORE");
            details = bundle.getString("DETAILS");
        }
    }

    private void setValues () {
        tvDateTime.setText(dateTime);
        tvRound.setText(round);
        tvTeam1.setText(team1);
        tvTeam2.setText(team2);
        tvScore.setText(score);
        tvDetails.setText(details);
        imgTeam1.setImageResource(Flags.getFlag(team1));
        imgTeam2.setImageResource(Flags.getFlag(team2));
    }
}
