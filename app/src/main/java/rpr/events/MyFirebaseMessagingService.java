package rpr.events;

/**
 * Created by naman on 22-04-2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    UserSessionManager session;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        // Check if message contains a data payload.
        session = new UserSessionManager(getApplicationContext());

        if(session.isUserLoggedIn()){
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Intent intent = new Intent(this, NavBar.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (remoteMessage.getData().get("event_id") != null && remoteMessage.getData().get("name") != null && remoteMessage.getData().get("time") != null && remoteMessage.getData().get("venue") != null && remoteMessage.getData().get("details") != null && remoteMessage.getData().get("usertype_id") != null && remoteMessage.getData().get("usertype") != null && remoteMessage.getData().get("creator_id") != null && remoteMessage.getData().get("creator") != null && remoteMessage.getData().get("category_id") != null && remoteMessage.getData().get("category") != null){
                    intent = new Intent(this, EventDisplayUser.class);

                    intent.putExtra("event_id", Integer.parseInt(remoteMessage.getData().get("event_id")));
                    intent.putExtra("name", remoteMessage.getData().get("name"));
                    intent.putExtra("time", remoteMessage.getData().get("time"));
                    intent.putExtra("venue", remoteMessage.getData().get("venue"));
                    intent.putExtra("details", remoteMessage.getData().get("details"));
                    intent.putExtra("usertype_id", Integer.parseInt(remoteMessage.getData().get("usertype_id")));
                    intent.putExtra("usertype", remoteMessage.getData().get("usertype"));
                    intent.putExtra("creator_id", Integer.parseInt(remoteMessage.getData().get("creator_id")));
                    intent.putExtra("creator", remoteMessage.getData().get("creator"));
                    intent.putExtra("category_id", Integer.parseInt(remoteMessage.getData().get("category_id")));
                    intent.putExtra("category", remoteMessage.getData().get("category"));

                }
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.event_icon)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("body"))
                        .setAutoCancel(true)
                        .setColor(ContextCompat.getColor(this,R.color.colorPrimary))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(Integer.parseInt(remoteMessage.getData().get("event_id")), notificationBuilder.build());


            }
        }



        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Intent intent = new Intent(this, NavBar.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                    PendingIntent.FLAG_ONE_SHOT);
//
//            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.drawable.ic_all_inclusive_black_24dp)
//                    .setContentTitle(remoteMessage.getNotification().getTitle())
//                    .setContentText(remoteMessage.getNotification().getBody())
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        // [END receive_message]
    }
}