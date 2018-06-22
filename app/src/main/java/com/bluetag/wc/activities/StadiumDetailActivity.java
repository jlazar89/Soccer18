package com.bluetag.wc.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bluetag.wc.R;
import com.bluetag.wc.fragments.DialogFullImage;
import com.bluetag.wc.fragments.StadiumDetailFragment;
import com.bluetag.wc.model.Stadium;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_IMAGE_URL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_STADIUM_MODEL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_TRANSITION_KEY_STADIUM_IMAGE;

/**
 * An activity representing a single Stadium detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StadiumListActivity}.
 */
public class StadiumDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle arguments = new Bundle();
                arguments.putString(BUNDLE_KEY_IMAGE_URL,
                        ((Stadium) getIntent().getSerializableExtra(BUNDLE_KEY_STADIUM_MODEL)).getStadiumImageUrl());
                FragmentManager fm = getSupportFragmentManager();
                DialogFullImage dialogFullImage = new DialogFullImage();
                dialogFullImage.setArguments(arguments);
                dialogFullImage.show(fm,DialogFullImage.class.getSimpleName());
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(BUNDLE_KEY_STADIUM_MODEL,getIntent().getSerializableExtra(BUNDLE_KEY_STADIUM_MODEL));
            arguments.putString(BUNDLE_TRANSITION_KEY_STADIUM_IMAGE,getIntent().getStringExtra(BUNDLE_TRANSITION_KEY_STADIUM_IMAGE));
            StadiumDetailFragment fragment = new StadiumDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
                    .commit();
        }
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
