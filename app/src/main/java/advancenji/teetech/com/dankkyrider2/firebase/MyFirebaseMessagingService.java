package advancenji.teetech.com.dankkyrider2.firebase;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import advancenji.teetech.com.dankkyrider2.MainActivity;
import advancenji.teetech.com.dankkyrider2.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

    }

    @Override
    public void onMessageReceived(RemoteMessage message)
    {
        Log.i("message",new JSONObject(message.getData()).toString());
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder  notificationBuilder =  new NotificationCompat.Builder(this,MainActivity.CHANNEL_ID);
        notificationBuilder.setContentText(message.getNotification().getBody())
                .setContentTitle("Dinkky Notification")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setColorized(true)
                //.setCustomContentView(RemoteViews.RemoteView.)
                .setSmallIcon(R.mipmap.ic_launcher_round);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(0,notificationBuilder.build());

    }

    @Override
    public void onMessageSent(String message)
    {

    }

    @Override
    public void onSendError(String error,Exception e)
    {

    }

}
