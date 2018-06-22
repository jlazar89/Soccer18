package com.bluetag.wc.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_NAME_PRONOUNCE;

/**
 * Created by Jeffy on 4/2/2017.
 */

public class TTSService extends Service implements TextToSpeech.OnInitListener {
    private String mStadiumName;
    private TextToSpeech mTts;
    private static final String TAG = "TTSService";

    @Override

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {

        mTts = new TextToSpeech(this,
                this  // OnInitListener
        );
        mTts.setPitch(2);
        mTts.setSpeechRate((float) 0.75);
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        mStadiumName=(String) intent.getExtras().get(BUNDLE_KEY_NAME_PRONOUNCE);

        sayHello(mStadiumName);
        super.onStart(intent, startId);
    }

    @Override
    public void onInit(int status) {
        Log.v(TAG, "oninit");
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.v(TAG, "Language is not available.");
            } else {

                sayHello(mStadiumName);

            }
        } else {
            Log.v(TAG, "Could not initialize TextToSpeech.");
        }
    }

    private void sayHello(String stadiumName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTts.speak(stadiumName,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            mTts.speak(stadiumName, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}

