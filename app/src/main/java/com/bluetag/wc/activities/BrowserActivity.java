package com.bluetag.wc.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bluetag.wc.R;
import com.bluetag.wc.WCApplication;
import com.bluetag.wc.api.rss.converter.RssItem;
import com.bluetag.wc.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_DETAIL_URL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_LIVE_SCORE;

public class BrowserActivity extends AppCompatActivity {

    // private String TAG = BrowserActivity.class.getSimpleName();
    private String url;
    private float m_downX;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.live_score_refresh)
    FloatingActionButton refresh;

    RssItem rssItem;

    private AdView mAdView;

    private InterstitialAd mInterstitialAd;
    private boolean isLiveScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.browser_back_interstital_ad_unit_id));
        WCApplication.requestNewInterstitial();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        url = getIntent().getStringExtra(BUNDLE_KEY_DETAIL_URL);
        isLiveScore = getIntent().getBooleanExtra(BUNDLE_KEY_LIVE_SCORE, false);

        if (isLiveScore) {
            getSupportActionBar().setTitle(getString(R.string.menu_live_scores));
            refresh.setVisibility(View.VISIBLE);
            mAdView.setVisibility(View.VISIBLE);
        } else {
            getSupportActionBar().setTitle("");
        }

        if (TextUtils.isEmpty(url)) {
            rssItem = (RssItem) getIntent().getSerializableExtra(BUNDLE_KEY_DETAIL_URL);
            url = rssItem.getLink();
        }

        // if no url is passed, close the activity
        if (TextUtils.isEmpty(url)) {
            finish();
        }

        initWebView();

        if (Utils.isConnected(BrowserActivity.this)) {
            webView.loadUrl(url);
        } else {
            Utils.showSnackBar(coordinatorLayout, getResources().getString(R.string.no_internet_connection));
        }


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

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("DBA681008FF649057E4250D6D754C0DD")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        WCApplication.showInterstitial();
    }

    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.loadUrl("about:blank");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView != null) {
                    webView.reload();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!isLiveScore) {
            getMenuInflater().inflate(R.menu.browser, menu);

            if (Utils.isBookmarked(this, webView.getUrl())) {
                // change icon color
                Utils.tintMenuIcon(getApplicationContext(), menu.getItem(0), R.color.colorAccent);
            } else {
                Utils.tintMenuIcon(getApplicationContext(), menu.getItem(0), android.R.color.white);
            }
        }
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // menu item 0-index is bookmark icon

        // enable - disable the toolbar navigation icons
        if (!isLiveScore) {
            if (!webView.canGoBack()) {
                menu.getItem(1).setEnabled(false);
                menu.getItem(1).getIcon().setAlpha(130);
            } else {
                menu.getItem(1).setEnabled(true);
                menu.getItem(1).getIcon().setAlpha(255);
            }

            if (!webView.canGoForward()) {
                menu.getItem(2).setEnabled(false);
                menu.getItem(2).getIcon().setAlpha(130);
            } else {
                menu.getItem(2).setEnabled(true);
                menu.getItem(2).getIcon().setAlpha(255);
            }
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (!isLiveScore) {
            if (item.getItemId() == android.R.id.home) {
                showInterstitial();
                finish();
            }

            if (item.getItemId() == R.id.action_bookmark) {
                // bookmark / unbookmark the url
                Utils.bookmarkUrl(this, webView.getUrl());

                String msg = Utils.isBookmarked(this, webView.getUrl()) ?
                        webView.getTitle() + "is Bookmarked!" :
                        webView.getTitle() + " removed!";
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
                snackbar.show();

                // refresh the toolbar icons, so that bookmark icon color changes
                // depending on bookmark status
                invalidateOptionsMenu();
            }

            if (item.getItemId() == R.id.action_back) {
                back();
            }

            if (item.getItemId() == R.id.action_forward) {
                forward();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        webView.onPause();
        webView.pauseTimers();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
    }


    @Override
    protected void onDestroy() {
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

    // backward the browser navigation
    private void back() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    // forward the browser navigation
    private void forward() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}
