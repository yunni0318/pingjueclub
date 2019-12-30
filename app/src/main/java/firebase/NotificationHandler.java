package firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;

import org.json.JSONObject;

import java.util.List;

public class NotificationHandler extends FirebaseMessagingService {

    private static String channel_name = "MAKE_QR_PAYMENT";
    private static String channel_description = "MAKE_QR_PAYMENT_DESCRIPTION";


    public NotificationHandler() {
    }


    @Override
    public void onNewToken(String token) {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);
        if (ud.size() > 0) {
            String table_name = UserDetails.getTableName(UserDetails.class);
            String loginid_field = StringUtil.toSQLName("LoginID");

            List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

            if (ud_list.size() > 0) {
                Bundle b = new Bundle();
                b.putString("access_token", ud_list.get(0).getAccessToken());
                b.putString("device_id", token);
                b.putInt(Api_Constants.COMMAND, Api_Constants.API_UPDATE_DEVICE_ID);

                new CallWebServices(Api_Constants.API_UPDATE_DEVICE_ID, this, false).execute(b);
            }
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().get("ServiceName").equals("MakeQRPayment")) {
                if (remoteMessage.getData().get("IsSuccess").equals("true")) {
                    sendNotification(remoteMessage);
                }
            }
        }

    }

    private void sendNotification(RemoteMessage remoteMessage) {

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
