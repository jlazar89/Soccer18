package com.bluetag.wc.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.bluetag.wc.MainActivity;
import com.bluetag.wc.R;

import butterknife.ButterKnife;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_DETAIL_URL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_LIVE_SCORE;
import static com.bluetag.wc.utils.Constants.FCM.MATCH_URL;

/**
 * Created by Jeffy on 6/17/2017.
 */

public class SplashActivity extends Activity {
    Intent intent;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(mContext, MainActivity.class);
                if (getIntent().getExtras() != null) {
                    intent.putExtra(BUNDLE_KEY_DETAIL_URL, getIntent().getStringExtra(MATCH_URL));
                    intent.putExtra(BUNDLE_KEY_LIVE_SCORE, true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
                finish();
            }
        }, 750);
    }
}
