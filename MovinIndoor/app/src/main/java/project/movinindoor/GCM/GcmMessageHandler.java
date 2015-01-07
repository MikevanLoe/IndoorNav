package project.movinindoor.GCM;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import project.movinindoor.MapsActivity;
import project.movinindoor.R;
import project.movinindoor.Readers.HttpJson;
import project.movinindoor.Readers.RepairReader;

/**
 * Created by Davey on 16-12-2014.
 */
public class GcmMessageHandler extends IntentService {

    String mes = "";
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

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(MapsActivity.getContext());
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("name");
        //Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("name"));
        //Log.i("GCM", "Received : "+ extras);
        sendPushNotification("New Repair", mes);
        try {
            MapsActivity.setJitems(new HttpJson().execute("http://movin.nvrstt.nl/defectsjson.php").get());
            try {
                MapsActivity.getSetupGraph().setRepairReader(new RepairReader());
            } catch (NullPointerException e) {}
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            //MapsActivity.setupGraph.setRepairReader(new RepairReader());
        } catch (NullPointerException e) { }
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    int notifyCount = 0;

    public void sendPushNotification(String title, String text) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(MapsActivity.getContext().NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, MapsActivity.class);
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.movin_push)

                .addAction(R.drawable.movin_push, "View", intent)
                .addAction(0, "Remind", intent)

                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;



        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
         //       | Intent.FLAG_ACTIVITY_SINGLE_TOP);



        notification.contentIntent = intent;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        long[] vibrate = { 0, 800, 800, 1200, 2000, 2000 };
        notification.vibrate = vibrate;

        notification.ledARGB = 0xFFF700FF;
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        notification.ledOnMS = 2000;
        notification.ledOffMS = 2000;
        int in = (int) Math.random();

        notificationManager.notify(in, notification);
    }

}
