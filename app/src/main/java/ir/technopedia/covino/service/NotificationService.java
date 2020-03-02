package ir.technopedia.covino.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import ir.technopedia.covino.R;
import ir.technopedia.covino.activity.MainActivity;
import ir.technopedia.covino.util.SharedPreferencesManager;


/**
 * Created by user1 on 12/14/2016.
 */

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgServiceDemo";

    SharedPreferencesManager sharedPreferencesManager;
    Intent notificationIntent;
    NotificationCompat.Builder mBuilder;
    NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                Map<String, String> map = remoteMessage.getData();
                handleDataMessage(map);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage() + "\n" + e.getCause() + "\n");
                e.printStackTrace();
            }
        }

    }

    public void handleDataMessage(Map<String, String> map) {

        notificationIntent = new Intent(this, MainActivity.class);

        showNotificationMessage(this, map.get("title"), map.get("message"));

    }

    private void showNotificationMessage(Context context, String title, String message) {

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = this.getResources().getString(R.string.default_notification_channel_id);
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.shield)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentTitle(title)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(message);

        mBuilder.setStyle(bigTextStyle);

        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());

    }

}