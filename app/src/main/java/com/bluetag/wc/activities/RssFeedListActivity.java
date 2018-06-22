package com.bluetag.wc.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bluetag.wc.R;
import com.bluetag.wc.WCApplication;
import com.bluetag.wc.adapter.RssItemsAdapter;
import com.bluetag.wc.api.rss.RssService;
import com.bluetag.wc.api.rss.converter.RssConverterFactory;
import com.bluetag.wc.api.rss.converter.RssFeed;
import com.bluetag.wc.api.rss.converter.RssItem;
import com.bluetag.wc.callback.OnRssItemClickListener;
import com.bluetag.wc.fragments.BrowserFragment;
import com.bluetag.wc.utils.AppPreferences;
import com.bluetag.wc.utils.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.bluetag.wc.utils.AppPreferences.Key.TOTAL_FEED_COUNT;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_DETAIL_URL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_RSS_MODEL;
import static com.bluetag.wc.utils.Constants.BundleKeys.FIFA_SITE;
import static com.bluetag.wc.utils.Constants.BundleKeys.KEY_FEED_URl;

/**
 * Created by Jeffy on 5/6/2017.
 */

public class RssFeedListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnRssItemClickListener {
    private static final String TAG = RssFeedListActivity.class.getSimpleName();

    private View recyclerView;
    private ProgressBar progressBar;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private RssItemsAdapter mAdapter;

    @BindView(R.id.swRefresh)
    SwipeRefreshLayout mSwRefresh;

    @BindView(R.id.recycler_list)
    RecyclerView mRecyclerView;

    // List of Native Express ads and MenuItems that populate the RecyclerView.
    private List<Object> mMultipleItems = new ArrayList<>();
    // The Native Express ad height.
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 120;

    private String mFeedUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_list);
        ButterKnife.bind(this);

        int intValue = getIntent().getIntExtra(KEY_FEED_URl, 0);
        String[] rssFeeds = getResources().getStringArray(R.array.rss_feeds);
        mFeedUrl = rssFeeds[intValue];

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar.setTitle(getTitle());

        if (intValue == 0)
            getSupportActionBar().setTitle(getResources().getString(R.string.title_rss_feed_news));
        else if (intValue == 1)
            getSupportActionBar().setTitle(getResources().getString(R.string.title_rss_feed_photos));
        else if (intValue == 2)
            getSupportActionBar().setTitle(getResources().getString(R.string.title_rss_feed_videos));


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAdapter = new RssItemsAdapter(RssFeedListActivity.this);
        mAdapter.setListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RssFeedListActivity.this));
        mRecyclerView.setAdapter(mAdapter);
        mSwRefresh.setOnRefreshListener(this);

        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            WCApplication.requestNewInterstitial();
            mTwoPane = true;
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fetchRss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(mTwoPane){
                WCApplication.showInterstitial();
            }
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

    /**
     * Fetches Rss Feed Url
     */
    public void fetchRss() {
        //mFeedUrl = "http://www.fifa.com/worldcup/news/rss.xml";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://github.com")
                .addConverterFactory(RssConverterFactory.create())
                .build();

        showLoading();
        RssService service = retrofit.create(RssService.class);
        service.getRss(mFeedUrl)
                .enqueue(new Callback<RssFeed>() {
                    @Override
                    public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                        onRssItemsLoaded(response.body().getItems());
                        String rss_first_date = response.body().getItem(0).getPublishDate();
                        AppPreferences.getInstance(RssFeedListActivity.this).put(TOTAL_FEED_COUNT, response.body().getItems().size());
                        hideLoading();
                    }

                    @Override
                    public void onFailure(Call<RssFeed> call, Throwable t) {
                        Toast.makeText(RssFeedListActivity.this, "Failed to fetchRss RSS feed!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    /**
     * Loads fetched {@link RssItem} list to RecyclerView
     *
     * @param rssItems
     */
    public void onRssItemsLoaded(List<RssItem> rssItems) {
        // Update the RecyclerView item's list with menu items and Native Express ads.
        addMenuItemsFromJson(rssItems);
        addNativeExpressAds();
        setUpAndLoadNativeExpressAds();
        mAdapter.setItems(mMultipleItems);
        mAdapter.notifyDataSetChanged();
        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     */
    private void addMenuItemsFromJson(List<RssItem> rssItems) {
        mMultipleItems.clear();
        for (RssItem rssItem : rssItems) {
            mMultipleItems.add(rssItem);
        }
    }

    /**
     * Adds Native Express ads to the items list.
     */
    private void addNativeExpressAds() {

        // Loop through the items array and place a new Native Express ad in every ith position in
        // the items List.
        for (int i = 0; i <= mMultipleItems.size(); i += Constants.ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(RssFeedListActivity.this);
            mMultipleItems.add(i, adView);
        }
    }

    /**
     * Sets up and loads the Native Express ads.
     */
    private void setUpAndLoadNativeExpressAds() {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                final float scale = RssFeedListActivity.this.getResources().getDisplayMetrics().density;
                // Set the ad size and ad unit ID for each Native Express ad in the items list.
                for (int i = 0; i <= mMultipleItems.size(); i += Constants.ITEMS_PER_AD) {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView) mMultipleItems.get(i);
                    final CardView cardView = (CardView) findViewById(R.id.ad_card_view);
                    int adWidth = 0;
                    if(cardView!=null){
                        adWidth = cardView.getWidth() - cardView.getPaddingLeft()
                                - cardView.getPaddingRight();
                    }
                    AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);
                    adView.setAdSize(AdSize.BANNER);
                    adView.setAdUnitId(getResources().getString(R.string.news_list_native_ad_unit_id));



                }

                // Load the first Native Express ad in the items list.
                loadNativeExpressAd(0);
            }
        });
    }

    /**
     * Loads the Native Express ads in the items list.
     */
    private void loadNativeExpressAd(final int index) {

        if (index >= mMultipleItems.size()) {
            return;
        }

        Object item = mMultipleItems.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadNativeExpressAd(index + Constants.ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index + Constants.ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest
                .Builder()
                .addTestDevice("C2F08886FF83705D832BC2C44EA40A91")
                .build());
    }

    /**
     * Shows {@link SwipeRefreshLayout}
     */
    public void showLoading() {
        mSwRefresh.setRefreshing(true);
    }


    /**
     * Hides {@link SwipeRefreshLayout}
     */
    public void hideLoading() {
        mSwRefresh.setRefreshing(false);
    }

    /**
     * Triggers on {@link SwipeRefreshLayout} refresh
     */
    @Override
    public void onRefresh() {
        fetchRss();
    }

    @Override
    public void onItemSelected(RssItem rssItem) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(BUNDLE_KEY_RSS_MODEL, rssItem);
            BrowserFragment fragment = new BrowserFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    //.addSharedElement(sharedImageView, ViewCompat.getTransitionName(sharedImageView))
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Context context = this;
            Intent intent = new Intent(context, BrowserActivity.class);

            if (rssItem.getLink().contains(FIFA_SITE)) {
                intent.putExtra(BUNDLE_KEY_DETAIL_URL, rssItem);
            } else {
                intent.putExtra(BUNDLE_KEY_DETAIL_URL, FIFA_SITE + rssItem.getLink());
            }


            // intent.putExtra(BUNDLE_TRANSITION_KEY_PAST_WINNER_IMAGE, ViewCompat.getTransitionName(sharedImageView));
//
            // ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            //         this,
            //         sharedImageView,
            //         ViewCompat.getTransitionName(sharedImageView));
//
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ////    context.startActivity(intent, options.toBundle());
            //} else {
            context.startActivity(intent);
            //}
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mTwoPane){
            WCApplication.showInterstitial();
        }
    }
}
