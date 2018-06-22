package com.bluetag.wc.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bluetag.wc.R;
import com.bluetag.wc.adapter.StadiumListAdapter;
import com.bluetag.wc.api.ApiClient;
import com.bluetag.wc.api.ApiInterface;
import com.bluetag.wc.callback.OnItemClickListener;
import com.bluetag.wc.fragments.StadiumDetailFragment;
import com.bluetag.wc.model.Stadium;
import com.bluetag.wc.model.StadiumModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_STADIUM_MODEL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_TRANSITION_KEY_STADIUM_IMAGE;

/**
 * An activity representing a list of Stadiums. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StadiumDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StadiumListActivity extends AppCompatActivity implements OnItemClickListener {
    private static final String TAG = StadiumListActivity.class.getSimpleName();
    private StadiumListAdapter stadiumListAdapter;
    private View recyclerView;
    private ProgressBar progressBar;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;


    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium_list);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_list);
        assert recyclerView != null;


        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        getStadiums();

    }

    void getStadiums() {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<StadiumModel> call = apiService.getStadiumsList();
        call.enqueue(new Callback<StadiumModel>() {
            @Override
            public void onResponse(Call<StadiumModel> call, Response<StadiumModel> response) {
                List<Stadium> stadiums = response.body().getStadiums();
                setupRecyclerView((RecyclerView) recyclerView, stadiums);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StadiumModel> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView,List<Stadium> stadiums) {
        stadiumListAdapter = new StadiumListAdapter(StadiumListActivity.this, stadiums);
        stadiumListAdapter.setClickListener(this);
        recyclerView.setAdapter(stadiumListAdapter);
    }

    @Override
    public void onItemClick(Stadium item,ImageView sharedImageView) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(BUNDLE_KEY_STADIUM_MODEL, item);
            StadiumDetailFragment fragment = new StadiumDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(sharedImageView, ViewCompat.getTransitionName(sharedImageView))
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Context context = this;
            Intent intent = new Intent(context, StadiumDetailActivity.class);
            intent.putExtra(BUNDLE_KEY_STADIUM_MODEL, item);
            intent.putExtra(BUNDLE_TRANSITION_KEY_STADIUM_IMAGE, ViewCompat.getTransitionName(sharedImageView));

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    sharedImageView,
                    ViewCompat.getTransitionName(sharedImageView));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                context.startActivity(intent, options.toBundle());
            }else{
                context.startActivity(intent);
            }
        }
    }
}
