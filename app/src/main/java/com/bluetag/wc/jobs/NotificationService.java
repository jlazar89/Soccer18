package com.bluetag.wc.jobs;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.bluetag.wc.MainActivity;
import com.bluetag.wc.R;
import com.bluetag.wc.activities.RssFeedListActivity;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_NEWS_NOTIFICATION;
import static com.bluetag.wc.utils.Constants.BundleKeys.KEY_FEED_URl;

public class NotificationService extends IntentService {
    public static final int NOTIFICATION_ID = 8;
    private static final String TAG = NotificationService.class.getSimpleName();
    private NotificationManager mNotificationManager;

    public NotificationService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String message = bundle.getString(BUNDLE_KEY_NEWS_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification) // notification icon
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)) // notification icon
                .setContentTitle(message) // title for notification
                .setContentText(getString(R.string.title_rss_feed_news)) // message for notification
                .setAutoCancel(true); // clear notification after click

        Intent notificationIntent = new Intent(this, RssFeedListActivity.class);
        notificationIntent.putExtra(KEY_FEED_URl, 0);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
