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
                startActivity(intent);
                finish();
            }
        }, 750);
    }
}
