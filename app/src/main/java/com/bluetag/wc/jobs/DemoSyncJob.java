package com.bluetag.wc.jobs;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.bluetag.wc.api.rss.RssService;
import com.bluetag.wc.api.rss.converter.RssConverterFactory;
import com.bluetag.wc.api.rss.converter.RssFeed;
import com.bluetag.wc.utils.AppPreferences;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.bluetag.wc.utils.AppPreferences.Key.TOTAL_FEED_COUNT;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_NEWS_NOTIFICATION;

/**
 * Created by Jeffy on 5/27/2017.
 */

public class DemoSyncJob extends Job {

    public static final String TAG = "job_demo_tag";

    private String mFeedUrl;

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        // run your job here
        fetchRss();
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        new JobRequest.Builder(DemoSyncJob.TAG)
                .setExecutionWindow(30_000L, 40_000L)
                .build()
                .schedule();
    }

    public static void schedulePeriodic() {
        new JobRequest.Builder(DemoSyncJob.TAG)
                .setPeriodic(TimeUnit.HOURS.toMillis(1), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                //.setPersisted(true)
                .build()
                .schedule();
    }

    /**
     * Fetches Rss Feed Url
     */
    private void fetchRss() {
        mFeedUrl = "http://www.fifa.com/worldcup/news/rss.xml";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://github.com")
                .addConverterFactory(RssConverterFactory.create())
                .build();

        RssService service = retrofit.create(RssService.class);
        service.getRss(mFeedUrl)
                .enqueue(new Callback<RssFeed>() {
                    @Override
                    public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                        int saved_count = AppPreferences.getInstance(getContext()).getInt(TOTAL_FEED_COUNT);
                        if (response.body().getItems().size() > saved_count) {
                            String rss_first_title = response.body().getItem(0).getTitle();

                            Intent notiIntent = new Intent(getContext(), NotificationService.class);
                            notiIntent.putExtra(BUNDLE_KEY_NEWS_NOTIFICATION,rss_first_title);
                            getContext().startService(notiIntent);
                            //save to shared preferences
                            AppPreferences.getInstance(getContext()).put(TOTAL_FEED_COUNT, response.body().getItems().size());
                        }

                    }

                    @Override
                    public void onFailure(Call<RssFeed> call, Throwable t) {

                    }
                });
    }

}