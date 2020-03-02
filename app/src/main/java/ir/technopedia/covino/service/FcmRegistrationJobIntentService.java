package ir.technopedia.covino.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

public class FcmRegistrationJobIntentService extends JobIntentService
{
    // Unique job ID for this service.
    static final int JOB_ID = 42;

    // Convenience method for enqueuing work in to this service.
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, FcmRegistrationJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // the code from IntentService.onHandleIntent() ...
    }
}