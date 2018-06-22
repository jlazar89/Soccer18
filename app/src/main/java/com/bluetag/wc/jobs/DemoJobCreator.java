package com.bluetag.wc.jobs;

import com.bluetag.wc.WCApplication;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Jeffy on 5/27/2017.
 */
public class DemoJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case DemoSyncJob.TAG:
                return new DemoSyncJob();
            default:
                return null;
        }
    }
}
