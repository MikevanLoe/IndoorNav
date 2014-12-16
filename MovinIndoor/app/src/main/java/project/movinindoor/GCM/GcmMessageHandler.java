package project.movinindoor.GCM;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import project.movinindoor.MapsActivity;
import project.movinindoor.R;

/**
 * Created by Davey on 16-12-2014.
 */
public class GcmMessageHandler extends IntentService {

    String mes;
    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("name");
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("name"));
        Log.i("GCM", "Received : "+ extras);
        sendPushNotification("New Repair" +  extras.getString("name"), extras.getString("name"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    int notifyCount = 0;

    public void sendPushNotification(String title, String text) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(MapsActivity.getContext().NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(MapsActivity.getContext())
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.movin_push)
                .build();

        notification.ledARGB = Color.BLUE;
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        notification.ledOnMS = 1000;
        notification.ledOffMS = 1000;

        notificationManager.notify(notifyCount++, notification);
    }

}
