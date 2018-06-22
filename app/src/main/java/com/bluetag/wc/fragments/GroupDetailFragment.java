package com.bluetag.wc.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bluetag.wc.R;
import com.bluetag.wc.adapter.TeamAdapter;
import com.bluetag.wc.api.ApiClient;
import com.bluetag.wc.api.ApiInterface;
import com.bluetag.wc.model.Group;
import com.bluetag.wc.model.Team;
import com.bluetag.wc.model.TeamModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_GROUP_NAME;

/**
 * Created by Jeffy on 5/28/2017.
 */

public class GroupDetailFragment extends Fragment {
    private static final String TAG = GroupDetailFragment.class.getSimpleName();
    /**
     * The dummy content this fragment is presenting.
     */
    private Group mItem;
    private View recyclerView;
    private ProgressBar progressBar;
    private TeamAdapter mTeamAdapter;

    private AdView mAdView;

    public GroupDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(BUNDLE_KEY_GROUP_NAME)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (Group) getArguments().getSerializable(BUNDLE_KEY_GROUP_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group_detail, container, false);

        mAdView = (AdView)rootView. findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //set Title to toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mItem.getGroupName());

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recycler_list);
        assert recyclerView != null;

        getGroupTeams();

        return rootView;
    }


    void getGroupTeams() {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TeamModel> call = apiService.getGroupTeams(Integer.parseInt(mItem.getGroupId()));
        call.enqueue(new Callback<TeamModel>() {
            @Override
            public void onResponse(Call<TeamModel> call, Response<TeamModel> response) {
                List<Team> teams = response.body().getTeams();
                setupRecyclerView((RecyclerView) recyclerView, teams);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TeamModel> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Team> teams) {
        mTeamAdapter = new TeamAdapter(getActivity(), teams);
        //mTeamAdapter.setClickListener(this);
        recyclerView.setAdapter(mTeamAdapter);
    }

}
