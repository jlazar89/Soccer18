package com.bluetag.wc;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.bluetag.wc.interfaces.LiveMatchCallback;
import com.bluetag.wc.jobs.DemoSyncJob;
import com.bluetag.wc.model.LiveMatchModel;
import com.bluetag.wc.model.MainScreenModel;
import com.bluetag.wc.utils.AutoFitGridLayoutManager;
import com.bluetag.wc.utils.NotificationReceiver;
import com.bluetag.wc.utils.NotificationUtils;
import com.bluetag.wc.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_DETAIL_URL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_LIVE_SCORE;
import static com.bluetag.wc.utils.Constants.BundleKeys.KEY_FEED_URl;
import static com.bluetag.wc.utils.Constants.BundleKeys.RssItems.RSS_FOR_NEWS;
import static com.bluetag.wc.utils.Constants.BundleKeys.RssItems.RSS_FOR_PHOTOS;
import static com.bluetag.wc.utils.Constants.BundleKeys.RssItems.RSS_FOR_VIDEOS;
import static com.bluetag.wc.utils.Constants.FCM.SHARED_PREF;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainScreenAdapter.ItemListener, LiveMatchCallback {

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

    //Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    private String liveUrl;
    private String match1Details;
    private String match2Details;
    private String liveUrl2;

    static String MATCHES_LINK = "https://jlazar89.github.io/json/matches.json";
    static boolean noData;
    DatabaseHelper dbHelper;

    LiveMatchModel liveMatchModel = new LiveMatchModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT));
        }

        // [END handle_data_extras]

        //Firebase Analytics
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, MainActivity.class.getSimpleName());
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        dbHelper = new DatabaseHelper(MainActivity.this);
        noData = dbHelper.retrieveMatchesData().getCount() == 0;
        retrieveDataFromJson();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        animationDrawable = (AnimationDrawable) coordinatorLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            DemoSyncJob.schedulePeriodic();

            // Fire off an intent to check if a TTS engine is installed
            Intent checkIntent = new Intent();
            checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkIntent, CHECK_TTS);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        //Real  time Database
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'live_match' node
        mFirebaseDatabase = mFirebaseInstance.getReference("worldcup-soccer-2018");

        //// store app title to 'app_title' node
        //mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        // app_title change listener
        mFirebaseInstance.getReference("live_match").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Match Url updated");

                liveUrl = dataSnapshot.getValue(String.class);
                liveMatchModel.setLive_match_1_url(liveUrl);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        // app_title change listener
        mFirebaseInstance.getReference("match_1_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Match 1 details updated");

                match1Details = dataSnapshot.getValue(String.class);
                liveMatchModel.setMatch_1_details(match1Details);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        //livematch 2
        mFirebaseInstance.getReference("live_match_2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Match Url updated");

                liveUrl2 = dataSnapshot.getValue(String.class);
                liveMatchModel.setLive_match_2_url(liveUrl2);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        // app_title change listener
        mFirebaseInstance.getReference("match_2_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Match 2 details updated");

                match2Details = dataSnapshot.getValue(String.class);
                liveMatchModel.setMatch_2_details(match2Details);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        setNotification();

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            if (getIntent().getStringExtra(BUNDLE_KEY_DETAIL_URL) != null) {
                liveUrl = getIntent().getStringExtra(BUNDLE_KEY_DETAIL_URL);
                openBrowserActivity(liveUrl);
            }
        }
    }

    private void setNotification() {
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
            calendar.set(Calendar.MINUTE, min + 2);
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

    // FUNCTION FOR RETRIEVE JSON DATA USING VOLLEY
    private void retrieveDataFromJson() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading data....");
        progressDialog.setCancelable(false);
        //progressDialog.show();
        //ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyle);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, MATCHES_LINK,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                String id = object.getString("id");
                                String date = object.getString("date");
                                String round = object.getString("round");
                                String team1 = object.getString("team1");
                                String team2 = object.getString("team2");
                                String score = object.getString("score");
                                String details = object.getString("details");
                                //v2
                                String status = object.getString("status");
                                if (noData) {
                                    saveMatchesData(id, date, round, team1, team2, score, details, status);
                                } else {
                                    updateMatchesData(id, date, round, team1, team2, score, details, status);
                                }
                            }

                            //populateRecyclerViewFromDB();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(getContext(), "Exception arises!!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.i("ERROR", error.getMessage());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();

        // register GCM registration complete receiver
        // LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
        //         new IntentFilter(REGISTRATION_COMPLETE));

        // // register new push message receiver
        // // by doing this, the activity will be notified each time a new message arrives
        // LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
        //         new IntentFilter(PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
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

    private void saveMatchesData(String id, String date, String round, String team1, String team2, String score, String details, String status) {
        boolean added = dbHelper.insertMatchesData(id, date, round, team1, team2, score, details, status);
        if (!added) {
            Toast.makeText(MainActivity.this, "Data can't be added!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMatchesData(String id, String date, String round, String team1, String team2, String score, String details, String status) {
        boolean updated = dbHelper.updateMatchesData(id, date, round, team1, team2, score, details, status);
        if (!updated) {
            Toast.makeText(MainActivity.this, "Doesn't updated!", Toast.LENGTH_SHORT).show();
        }
    }

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
            } else if (id == R.id.nav_settings) {
                SettingsFragment fragment = new SettingsFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        //.addSharedElement(sharedImageView, ViewCompat.getTransitionName(sharedImageView))
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(SettingsFragment.class.getSimpleName())
                        .commit();


                FragmentManager fragmentManager = getSupportFragmentManager();

            } else if (id == R.id.nav_your_teams) {
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
                if (!TextUtils.isEmpty(liveUrl) && !TextUtils.isEmpty(liveUrl2)) {
                    // Create the fragment and show it as a dialog.
                    LiveMatchDialog newFragment = LiveMatchDialog.newInstance(liveMatchModel);
                    newFragment.setMatchCallback(this);
                    newFragment.show(getSupportFragmentManager(), "dialog");
                } else if (!TextUtils.isEmpty(liveUrl)) {
                    openBrowserActivity(liveUrl);
                }
            }
        } else {
            Utils.showSnackBar(coordinatorLayout, getResources().getString(R.string.no_internet_connection));
        }

    }

    @Override
    public void onLiveMatchSelected(String matchurl) {
        openBrowserActivity(matchurl);
    }

    void openBrowserActivity(String url) {
        Intent i = new Intent(MainActivity.this, BrowserActivity.class);
        i.putExtra(BUNDLE_KEY_DETAIL_URL, url);
        i.putExtra(BUNDLE_KEY_LIVE_SCORE, true);
        startActivity(i);
    }
}
