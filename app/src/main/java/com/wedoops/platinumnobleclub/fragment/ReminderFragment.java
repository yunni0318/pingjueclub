package com.wedoops.platinumnobleclub.fragment;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.adapters.NotificationAdapter;
import com.wedoops.platinumnobleclub.adapters.ReminderAdapter;

import firebase.ReminderBroadcast;

import static android.content.Context.POWER_SERVICE;

public class ReminderFragment extends Fragment {

    private static Context get_context;
    private static View view;
    private static RecyclerView recyclerView_reminder;
    private static ReminderAdapter reminderAdapter;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public Button remind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reminder, container, false);
        get_context = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewByID();
        setupAdapter();
        //pushReminder();
    }

    private void setupViewByID() {
        recyclerView_reminder = view.findViewById(R.id.RecyclerView_reminder);
        remind = view.findViewById(R.id.remind);
//
//        Intent myIntent = new Intent();
//        myIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//        startActivity(myIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = get_context.getPackageName();
            PowerManager pm = (PowerManager) get_context.getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }

        createNotiChannel();
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(get_context, ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(get_context, 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) get_context.getSystemService(Context.ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();
                long tenSecondsInMillis = 1000 * 2;
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        timeAtButtonClick + tenSecondsInMillis, pendingIntent);
            }
        });
    }

    private void createNotiChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CHANNEL_NAME";
            String description = "DESCRIPTION";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setupAdapter() {
        reminderAdapter = new ReminderAdapter(get_context);
        RecyclerView.LayoutManager talent_mLayoutManager = new LinearLayoutManager(get_context, LinearLayoutManager.VERTICAL, false);
        recyclerView_reminder.setLayoutManager(talent_mLayoutManager);
        recyclerView_reminder.setAdapter(reminderAdapter);
        recyclerView_reminder.setNestedScrollingEnabled(false);
        reminderAdapter.notifyDataSetChanged();
    }

    private void pushReminder() {

        String message ="Hi! Let's go party loh!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(get_context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Reminder")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CHANNEL_NAME";
            String description = "DESCRIPTION";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(get_context);
        notificationManager.notify(0, builder.build());

    }

//    public void scheduleNotification(Context context, long delay, int notificationId) {//delay is after how much time(in millis) from current time you want to schedule the notification
//
//        String message ="Hi! Let's go party loh!";
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setContentTitle("Reminder")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSmallIcon(R.drawable.ic_launcher)
////                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.app_icon)).getBitmap())
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
//        Intent intent = new Intent(context, ReminderFragment.class);
//        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(activity);
//
//        Notification notification = builder.build();
//
//        Intent notificationIntent = new Intent(context, ReminderFragment.class);
//        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
//        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//    }
//
//    public static class MyNotificationPublisher extends BroadcastReceiver {
//
//        public static String NOTIFICATION_ID = "notification_id";
//        public static String NOTIFICATION = "notification";
//
//        @Override
//        public void onReceive(final Context context, Intent intent) {
//
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//            Notification notification = intent.getParcelableExtra(NOTIFICATION);
//            int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
//            notificationManager.notify(notificationId, notification);
//        }
//    }
}