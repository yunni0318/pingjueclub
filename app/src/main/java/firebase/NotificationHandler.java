package firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;
import com.wedoops.platinumnobleclub.helper.CONSTANTS_VALUE;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class NotificationHandler extends FirebaseMessagingService {

    private static String channel_name = "MAKE_QR_PAYMENT";
    private static String channel_description = "MAKE_QR_PAYMENT_DESCRIPTION";
    private static String notiImage, notiMessage, notiTitle, isSuccess;
    private static Bitmap remote_picture = null;

    public NotificationHandler() {
    }

    @Override
    public void onNewToken(String token) {

        super.onNewToken(token);
        Log.d("Token", token);
        new ApplicationClass(getApplicationContext()).writeIntoSharedPreferences(getApplicationContext(), CONSTANTS_VALUE.SHAREDPREFECENCE_NEW_TOKEN, token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            try {
                if (remoteMessage.getData().get("ServiceName").equals("MakeQRPayment")) {
                    if (remoteMessage.getData().get("IsSuccess").equals("true")) {
                        sendNotificationPayment(remoteMessage);
                    }
                }
                else if(remoteMessage.getData().get("ServiceName").equals("Reservation")){
                    if (remoteMessage.getData().get("IsSuccess").equals("true")) {
                        sendNotificationReservation(remoteMessage);
                    }
                }
                else {
                    if (remoteMessage.getData().get("IsSuccess").equals("true")) {
                        sendNotificationPayment(remoteMessage);
                    }
                }
            } catch (Exception e) {
                try {

                    Map<String, String> data = remoteMessage.getData();
                    notiTitle = data.get("Title");
                    isSuccess = data.get("IsSuccess");
                    notiMessage = data.get("Message");
                    notiImage = data.get("image");

                    if (notiImage == null) {
                        sendNotification(remoteMessage);
                    } else {
                        sendImageNotification(remoteMessage);
                     }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void sendNotificationPayment(RemoteMessage remoteMessage) {

        JSONObject SuccessMessage_Object = convertResponseToJsonObject(remoteMessage.getData().get("SuccessMessage"));

        String success_message;
        String bodyTitle;

        try {
            success_message = SuccessMessage_Object.getString("MessageEN");
            bodyTitle = remoteMessage.getNotification().getBody();

        } catch (Exception e) {
            success_message = "";
            bodyTitle = "";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(bodyTitle)
                .setContentText(success_message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(success_message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        createNotificationChannel();


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());

    }

    private void sendNotificationReservation(RemoteMessage remoteMessage) {

        JSONObject SuccessMessage_Object = convertResponseToJsonObject(remoteMessage.getData().get("SuccessMessage"));

        String en_message;
        String bodyTitle;

        try {
            en_message = SuccessMessage_Object.getString("MessageEN");
            bodyTitle = remoteMessage.getNotification().getTitle();

        } catch (Exception e) {
            en_message = "";
            bodyTitle = "";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(bodyTitle)
                .setContentText(en_message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(en_message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        createNotificationChannel();


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notiTitle)
                .setContentText(notiMessage)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notiMessage))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        createNotificationChannel();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());

    }

    private void sendImageNotification(RemoteMessage remoteMessage) {

        try {
            URL url = new URL(notiImage);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            remote_picture = BitmapFactory.decodeStream(input);

        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notiTitle)
                .setContentText(notiMessage)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(remote_picture))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setLargeIcon(remote_picture)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        createNotificationChannel();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CHANNEL_NAME";
            String description = "DESCRIPTION";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static JSONObject convertResponseToJsonObject(String responses) {
        JSONObject jObject;

        try {
            jObject = new JSONObject(responses);

        } catch (Exception e) {
            jObject = new JSONObject();
        }
        return jObject;
    }

}
