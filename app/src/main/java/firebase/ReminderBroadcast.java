package firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.wedoops.platinumnobleclub.R;

public class ReminderBroadcast extends BroadcastReceiver {

    Bitmap remote_picture = null;
    String notiImage = "https://admin.platinumnobleclub.com/Images/Notification/test1vds_2_20210605164449.png";

    @Override
    public void onReceive(Context context, Intent intent) {

        remote_picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_noti_url);

        String message = "Hi! Let's go party loh!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "10001")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Reminder")
                .setContentText(message)
                .setLargeIcon(remote_picture)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(remote_picture))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        builder.setChannelId("10001");
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());
    }
}
