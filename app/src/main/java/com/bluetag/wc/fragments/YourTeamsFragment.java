package com.bluetag.wc.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.adapter.RecyclerViewAdapter;
import com.bluetag.wc.db.DatabaseHelper;
import com.bluetag.wc.model.MatchesListItems;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dream Land on 3/10/2018.
 */

public class YourTeamsFragment extends Fragment {

    LinearLayout ll;
    TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.your_teams_fragment, container, false);

        ll = rootView.findViewById(R.id.LLYourTeams);
        tv = rootView.findViewById(R.id.TVYourTeam);

        retrivalFunc();

        return rootView;
    }

    private void retrivalFunc() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        Cursor c = databaseHelper.getMyTeams();

        if (c.getCount() == 0) {
            tv.setVisibility(View.VISIBLE);
            ll.setVisibility(View.INVISIBLE);
        } else {
            tv.setVisibility(View.INVISIBLE);
            ll.setVisibility(View.VISIBLE);
            while (c.moveToNext()) {
                retrieveData(c.getString(0));
            }
        }
    }

    private void retrieveData(String name) {
        TextView textView = new TextView(getContext());
        textView.setText(name);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(17);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(5, 5, 5, 5);
        textView.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        ll.addView(textView);
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            Cursor cursor = databaseHelper.retrieveMatchesData();
            while (cursor.moveToNext()) {
                String team1 = cursor.getString(3);
                String team2 = cursor.getString(4);
                if (name.equals(team1) || name.equals(team2)) {
                    String id = cursor.getString(0);
                    String date = cursor.getString(1);
                    String round = cursor.getString(2);
                    String score = cursor.getString(5);
                    String details = cursor.getString(6);
                    //v2
                    String status= cursor.getString(7);
                    RecyclerView recyclerView = new RecyclerView(getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setClipToPadding(false);
                    recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    MatchesListItems list = new MatchesListItems(id, date, round, team1, team2, score, details,status);
                    List<MatchesListItems> matchesListItems = new ArrayList<>();
                    matchesListItems.add(list);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(matchesListItems, getContext(), "Matches");
                    recyclerView.setAdapter(adapter);
                    ll.addView(recyclerView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
