package com.bluetag.wc.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.WCApplication;
import com.bluetag.wc.adapter.GroupListAdapter;
import com.bluetag.wc.api.ApiClient;
import com.bluetag.wc.api.ApiInterface;
import com.bluetag.wc.callback.OnGroupItemClickListener;
import com.bluetag.wc.fragments.GroupDetailFragment;
import com.bluetag.wc.model.Group;
import com.bluetag.wc.model.GroupModel;
import com.bluetag.wc.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_GROUP_NAME;

/**
 * Created by Jeffy on 5/24/2017.
 */

public class GroupListActivity extends AppCompatActivity implements OnGroupItemClickListener {
    private static final String TAG = GroupListActivity.class.getSimpleName();
    private GroupListAdapter groupListAdapter;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_list)
    View recyclerView;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private AdView mAdView;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.group_back_interstitial_ad_unit_id));
        WCApplication.requestNewInterstitial();

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        assert recyclerView != null;
        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (Utils.isConnected(GroupListActivity.this)) {
            getGroups();
        } else {
            Utils.showSnackBar(coordinatorLayout, getResources().getString(R.string.no_internet_connection));
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("DBA681008FF649057E4250D6D754C0DD")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    void getGroups() {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<GroupModel> call = apiService.getGroups();
        call.enqueue(new Callback<GroupModel>() {
            @Override
            public void onResponse(Call<GroupModel> call, Response<GroupModel> response) {
                List<Group> stadiums = response.body().getGroups();
                setupRecyclerView((RecyclerView) recyclerView, stadiums);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GroupModel> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Group> groups) {
        groupListAdapter = new GroupListAdapter(GroupListActivity.this, groups);
        groupListAdapter.setClickListener(this);
        recyclerView.setAdapter(groupListAdapter);
    }


    @Override
    public void onItemClick(Group item, TextView sharedView) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(BUNDLE_KEY_GROUP_NAME, item);
            GroupDetailFragment fragment = new GroupDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    //.addSharedElement(sharedImageView, ViewCompat.getTransitionName(sharedImageView))
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Context context = this;
            Intent intent = new Intent(context, GroupDetailActivity.class);
            intent.putExtra(BUNDLE_KEY_GROUP_NAME, item);
            //intent.putExtra(BUNDLE_TRANSITION_KEY_STADIUM_IMAGE, ViewCompat.getTransitionName(sharedImageView));

            context.startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            WCApplication.showInterstitial();
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        WCApplication.showInterstitial();
        super.onBackPressed();
    }


    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            //startGame();
        }
    }
}
