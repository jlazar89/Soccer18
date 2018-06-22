package com.bluetag.wc.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bluetag.wc.R;
import com.bluetag.wc.api.rss.converter.RssItem;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_RSS_MODEL;
import static com.bluetag.wc.utils.Constants.BundleKeys.FIFA_SITE;

/**
 * Created by Jeffy on 5/6/2017.
 */

public class BrowserFragment extends Fragment {

    /**
     * The dummy content this fragment is presenting.
     */
    private RssItem mItem;
    private WebView webView;
    private ProgressBar progressBar;
    private float m_downX;
    public BrowserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(BUNDLE_KEY_RSS_MODEL)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (RssItem) getArguments().getSerializable(BUNDLE_KEY_RSS_MODEL);
            //String transitionName = getArguments().getString(BUNDLE_TRANSITION_KEY_PAST_WINNER_IMAGE);
            //postponeEnterTransition();
           // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           //     setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
           // }

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("News");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browser, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            webView = (WebView) rootView.findViewById(R.id.webView);
            progressBar = (ProgressBar)rootView. findViewById(R.id.progressBar);

            initWebView();

            if(mItem.getLink().contains(FIFA_SITE)){
                webView.loadUrl(mItem.getLink());
            }else{
                webView.loadUrl(FIFA_SITE+mItem.getLink());
            }
        }
        return rootView;
    }

    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(getActivity()));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                //invalidateOptionsMenu();
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
                //invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
                //invalidateOptionsMenu();
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
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
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
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

}
