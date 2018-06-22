package com.bluetag.wc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.bluetag.wc.activities.BrowserActivity;
import com.bluetag.wc.activities.GroupListActivity;
import com.bluetag.wc.activities.MascotActivity;
import com.bluetag.wc.activities.PastWinnersListActivity;
import com.bluetag.wc.activities.RssFeedListActivity;
import com.bluetag.wc.activities.StadiumListActivity;
import com.bluetag.wc.activities.StagesListActivity;
import com.bluetag.wc.adapter.MainScreenAdapter;
import com.bluetag.wc.db.DatabaseHelper;
import com.bluetag.wc.fragments.SettingsFragment;
import com.bluetag.wc.fragments.YourTeamsFragment;
import com.bluetag.wc.jobs.DemoSyncJob;
import com.bluetag.wc.model.MainScreenModel;
import com.bluetag.wc.utils.AutoFitGridLayoutManager;
import com.bluetag.wc.utils.NotificationReceiver;
import com.bluetag.wc.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_DETAIL_URL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_LIVE_SCORE;
import static com.bluetag.wc.utils.Constants.BundleKeys.KEY_FEED_URl;
import static com.bluetag.wc.utils.Constants.BundleKeys.RssItems.RSS_FOR_NEWS;
import static com.bluetag.wc.utils.Constants.BundleKeys.RssItems.RSS_FOR_PHOTOS;
import static com.bluetag.wc.utils.Constants.BundleKeys.RssItems.RSS_FOR_VIDEOS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainScreenAdapter.ItemListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final int CHECK_TTS = 100;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    ArrayList arrayList;
    private AnimationDrawable animationDrawable;

    private AdView mAdView;

    //Firebase RealTime Database
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String liveUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        animationDrawable = (AnimationDrawable) coordinatorLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DemoSyncJob.schedulePeriodic();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Fire off an intent to check if a TTS engine is installed
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, CHECK_TTS);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        arrayList = new ArrayList();
        arrayList.add(new MainScreenModel(getString(R.string.menu_groups_groups), R.drawable.ic_action_groups, "#09A9FF"));
        arrayList.add(new MainScreenModel(getString(R.string.menu_live_scores), R.drawable.ic_action_live_score, "#09A9FF"));
        arrayList.add(new MainScreenModel(getString(R.string.menu_groups_schedules), R.drawable.ic_action_schedules, "#3E51B1"));
        arrayList.add(new MainScreenModel(getString(R.string.menu_media_news), R.drawable.ic_action_news, "#0A9B88"));
        arrayList.add(new MainScreenModel(getString(R.string.menu_media_photos), R.drawable.ic_action_photos, "#0A9B88"));
        //arrayList.add(new MainScreenModel(getString(R.string.menu_media_videos), R.drawable.ic_action_video, "#0A9B88"));
        arrayList.add(new MainScreenModel(getString(R.string.menu_extras_stadiums), R.drawable.ic_action_stadium, "#673BB7"));
        arrayList.add(new MainScreenModel(getString(R.string.menu_extras_mascot), R.drawable.ic_action_mascot, "#4BAA50"));
        arrayList.add(new MainScreenModel(getString(R.string.menu_extras_past_winners), R.drawable.ic_action_winner, "#F94336"));

        MainScreenAdapter adapter = new MainScreenAdapter(MainActivity.this, arrayList, this);
        recyclerView.setAdapter(adapter);


        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            // do something
            /**
             AutoFitGridLayoutManager that auto fits the cells by the column width defined.
             **/

            AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 600);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            // do something else
            /**
             AutoFitGridLayoutManager that auto fits the cells by the column width defined.
             **/

            AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 300);
            recyclerView.setLayoutManager(layoutManager);

        }
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /**
         Simple GridLayoutManager that spans two columns
         **/
        //GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);

        // get reference to 'live_match' node
        mFirebaseDatabase = mFirebaseInstance.getReference("live_match");

        //// store app title to 'app_title' node
        //mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        // app_title change listener
        mFirebaseInstance.getReference("live_match").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                liveUrl = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });


        setNotification();
    }

    private void setNotification () {
        boolean alarmActive = (PendingIntent.getBroadcast(
                this,
                100,
                new Intent(this, NotificationReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        Calendar c = Calendar.getInstance();
        int min = c.get(Calendar.MINUTE);

        if (!alarmActive) {
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeInMillis(System.currentTimeMillis());
            //calendar.set(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, min+2);
            //calendar.set(Calendar.SECOND, 30);

            Intent intent = new Intent(this, NotificationReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    100,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//   @Override
//   public boolean onCreateOptionsMenu(Menu menu) {
//       // Inflate the menu; this adds items to the action bar if it is present.
//      // getMenuInflater().inflate(R.menu.main, menu);
//       return true;
//   }

//   @Override
//   public boolean onOptionsItemSelected(MenuItem item) {
//       // Handle action bar item clicks here. The action bar will
//       // automatically handle clicks on the Home/Up button, so long
//       // as you specify a parent activity in AndroidManifest.xml.
//       int id = item.getItemId();

//       //noinspection SimplifiableIfStatement
//       if (id == R.id.action_settings) {
//           return true;
//       }

//       return super.onOptionsItemSelected(item);
//   }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (Utils.isConnected(MainActivity.this)) {

            if (id == R.id.rate_app) {
                // Rate app
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(intent);
            } else if (id == R.id.share_app) {
                // Share apps
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            } else if (id == R.id.more_apps) {
                //More apps
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:BlueTag"));
                startActivity(intent);
            }else if(id==R.id.nav_settings){
                SettingsFragment fragment = new SettingsFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        //.addSharedElement(sharedImageView, ViewCompat.getTransitionName(sharedImageView))
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(SettingsFragment.class.getSimpleName())
                        .commit();


                FragmentManager fragmentManager = getSupportFragmentManager();

            }else if(id==R.id.nav_your_teams){
                YourTeamsFragment fragment = new YourTeamsFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        //.addSharedElement(sharedImageView, ViewCompat.getTransitionName(sharedImageView))
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(YourTeamsFragment.class.getSimpleName())
                        .commit();


                FragmentManager fragmentManager = getSupportFragmentManager();
            }
        } else {
            Utils.showSnackBar(coordinatorLayout, getResources().getString(R.string.no_internet_connection));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_TTS) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    @Override
    public void onItemClick(MainScreenModel item) {
        if (Utils.isConnected(MainActivity.this)) {

            if (item.text.equals(getString(R.string.title_group_list))) {
                // Handle the camera action
                Intent i = new Intent(MainActivity.this, GroupListActivity.class);
                startActivity(i);
            } else if (item.text.equals(getString(R.string.menu_groups_schedules))) {
                Intent i = new Intent(MainActivity.this, StagesListActivity.class);
                startActivity(i);
            } else if (item.text.equals(getString(R.string.menu_extras_stadiums))) {
                Intent i = new Intent(MainActivity.this, StadiumListActivity.class);
                startActivity(i);
            } else if (item.text.equals(getString(R.string.menu_extras_mascot))) {
                Intent i = new Intent(MainActivity.this, MascotActivity.class);
                startActivity(i);
            } else if (item.text.equals(getString(R.string.menu_extras_past_winners))) {
                Intent i = new Intent(MainActivity.this, PastWinnersListActivity.class);
                startActivity(i);
            } else if (item.text.equals(getString(R.string.menu_media_news))) {
                Intent i = new Intent(MainActivity.this, RssFeedListActivity.class);
                i.putExtra(KEY_FEED_URl, RSS_FOR_NEWS);
                startActivity(i);
            } else if (item.text.equals(getString(R.string.menu_media_photos))) {
                Intent i = new Intent(MainActivity.this, RssFeedListActivity.class);
                i.putExtra(KEY_FEED_URl, RSS_FOR_PHOTOS);
                startActivity(i);
            } else if (item.text.equals(getString(R.string.menu_media_videos))) {
                Intent i = new Intent(MainActivity.this, RssFeedListActivity.class);
                i.putExtra(KEY_FEED_URl, RSS_FOR_VIDEOS);
                startActivity(i);
            }//livescore
            else if (item.text.equals(getString(R.string.menu_live_scores))) {
                Intent i = new Intent(MainActivity.this, BrowserActivity.class);
                i.putExtra(BUNDLE_KEY_DETAIL_URL, liveUrl);
                i.putExtra(BUNDLE_KEY_LIVE_SCORE, true);
                startActivity(i);
            }
        } else {
            Utils.showSnackBar(coordinatorLayout, getResources().getString(R.string.no_internet_connection));
        }

    }
}
