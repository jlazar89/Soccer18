package com.bluetag.wc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.fragments.DialogFullImage;
import com.bluetag.wc.model.StadiumModel;
import com.bluetag.wc.utils.TTSService;
import com.bluetag.wc.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_IMAGE_URL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_DETAIL_URL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_STADIUM_MODEL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_NAME_PRONOUNCE;

/**
 * Created by Jeffy on 4/2/2017.
 */

public class MascotActivity extends AppCompatActivity {

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascot);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle arguments = new Bundle();
                arguments.putString(BUNDLE_KEY_IMAGE_URL, getResources().getString(R.string.mascot_image_url));
                FragmentManager fm = getSupportFragmentManager();
                DialogFullImage dialogFullImage = new DialogFullImage();
                dialogFullImage.setArguments(arguments);
                dialogFullImage.show(fm, DialogFullImage.class.getSimpleName());
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle( getResources().getString(R.string.mascot_name));
        }

        TextView readMore = (TextView) findViewById(R.id.mascot_more_Detail);
        SpannableString content = new SpannableString(getResources().getString(R.string.read_more));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        readMore.setText(content);
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MascotActivity.this, BrowserActivity.class);
                intent.putExtra(BUNDLE_KEY_DETAIL_URL, getResources().getString(R.string.mascot_detail_url));
                startActivity(intent);
            }
        });

        ImageView speak_name = (ImageView) findViewById(R.id.mascot_name_pronounce);
        speak_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut();
            }
        });

        ImageView stadium_backdrop = (ImageView)findViewById(R.id.mascot_backdrop);
        Picasso.with(MascotActivity.this)
                .load(getResources().getString(R.string.mascot_image_url))
                .placeholder(R.drawable.ic_placeholder)
                .into(stadium_backdrop);
    }

    private void speakOut() {
        Intent serviceIntent = new Intent(MascotActivity.this, TTSService.class);
        serviceIntent.putExtra(BUNDLE_KEY_NAME_PRONOUNCE, getResources().getString(R.string.mascot_name));
        startService(serviceIntent);
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

