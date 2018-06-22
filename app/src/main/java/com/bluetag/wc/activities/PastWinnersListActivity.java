package com.bluetag.wc.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bluetag.wc.R;
import com.bluetag.wc.adapter.PastWinnerListAdapter;
import com.bluetag.wc.api.ApiClient;
import com.bluetag.wc.api.ApiInterface;
import com.bluetag.wc.callback.OnPastWinnerItemClickListener;
import com.bluetag.wc.fragments.PastWinnerDetailFragment;
import com.bluetag.wc.model.PastWinner;
import com.bluetag.wc.model.PastWinnersModel;
import com.bluetag.wc.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_PAST_WINNER_MODEL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_TRANSITION_KEY_PAST_WINNER_IMAGE;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class PastWinnersListActivity extends AppCompatActivity implements OnPastWinnerItemClickListener {


    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_list)
    RecyclerView mRecyclerView;

    private PastWinnerListAdapter mTimeLineAdapter;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_winners);
        ButterKnife.bind(this);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        if(Utils.isConnected(PastWinnersListActivity.this)){
            getPastWinners();
        }else{
            Utils.showSnackBar(coordinatorLayout,getResources().getString(R.string.no_internet_connection));
        }
    }

    private LinearLayoutManager getLinearLayoutManager() {

        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    void getPastWinners() {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<PastWinnersModel> call = apiService.getPastWinnersList();
        call.enqueue(new Callback<PastWinnersModel>() {
            @Override
            public void onResponse(Call<PastWinnersModel> call, Response<PastWinnersModel> response) {
                List<PastWinner> pastWinners = response.body().getPastWinners();
                setupRecyclerView((RecyclerView) mRecyclerView, pastWinners);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PastWinnersModel> call, Throwable t) {
                // Log error here since request failed
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<PastWinner> pastWinners) {
        mTimeLineAdapter = new PastWinnerListAdapter(pastWinners);
        mTimeLineAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(PastWinner item, ImageView sharedImageView) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(BUNDLE_KEY_PAST_WINNER_MODEL, item);
            PastWinnerDetailFragment fragment = new PastWinnerDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(sharedImageView, ViewCompat.getTransitionName(sharedImageView))
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Context context = this;
            Intent intent = new Intent(context, PastWinnerDetailActivity.class);
            intent.putExtra(BUNDLE_KEY_PAST_WINNER_MODEL, item);
            intent.putExtra(BUNDLE_TRANSITION_KEY_PAST_WINNER_IMAGE, ViewCompat.getTransitionName(sharedImageView));
//
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    sharedImageView,
                    ViewCompat.getTransitionName(sharedImageView));
//
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                context.startActivity(intent, options.toBundle());
            } else {
                context.startActivity(intent);
            }
        }
    }
}
