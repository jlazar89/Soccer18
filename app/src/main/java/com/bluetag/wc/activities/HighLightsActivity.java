package com.bluetag.wc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bluetag.wc.R;
import com.bluetag.wc.adapter.GroupListAdapter;
import com.bluetag.wc.adapter.HighLightsAdapter;
import com.bluetag.wc.api.ApiClient;
import com.bluetag.wc.api.ApiInterface;
import com.bluetag.wc.callback.OnHighLighItemClickListener;
import com.bluetag.wc.interfaces.RecyclerViewOnClickListener;
import com.bluetag.wc.model.Group;
import com.bluetag.wc.model.HighLightsModel;
import com.bluetag.wc.model.YoutubeVideoModel;
import com.bluetag.wc.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HighLightsActivity extends AppCompatActivity implements OnHighLighItemClickListener {
    private static final String TAG = HighLightsActivity.class.getSimpleName();
    private HighLightsAdapter highLightsAdapter;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_list)
    View recyclerView;

    @BindView(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights);
        ButterKnife.bind(this);

        //Banner ads
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (Utils.isConnected(HighLightsActivity.this)) {
            getHighlights();
        } else {
            Utils.showSnackBar(coordinatorLayout, getResources().getString(R.string.no_internet_connection));
        }
    }

    void getHighlights() {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<HighLightsModel> call = apiService.getHighlights();
        call.enqueue(new Callback<HighLightsModel>() {
            @Override
            public void onResponse(Call<HighLightsModel> call, Response<HighLightsModel> response) {
                List<HighLightsModel.HighLight> stadiums = response.body().getHighlights();
                setupRecyclerView((RecyclerView) recyclerView, stadiums);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<HighLightsModel> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<HighLightsModel.HighLight> highlights) {
        highLightsAdapter = new HighLightsAdapter(HighLightsActivity.this, highlights);
        highLightsAdapter.setClickListener(this);
        recyclerView.setAdapter(highLightsAdapter);
    }

    @Override
    public void onItemClick(String VideoId) {
        //start youtube player activity by passing selected video id via intent
        startActivity(new Intent(HighLightsActivity.this, YoutubePlayerActivity.class)
                .putExtra("video_id", VideoId));
    }
}
