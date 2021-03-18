package com.wedoops.platinumnobleclub;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Calendar;

import static androidx.core.content.ContextCompat.checkSelfPermission;


public class ReservationFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS_CODE_WRITE_CALENDAR = 1;
    private View view;
    private long _eventId;
    private Context app;


    public ReservationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = getContext();
//        if (checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED &&
//                checkSelfPermission(getContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//
//            requestPermissions(new String[]{android.Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, REQUEST_PERMISSIONS_CODE_WRITE_CALENDAR);
//
//        } else {
//            long calID = 3;
//            long startMillis = 0;
//            long endMillis = 0;
//            Calendar beginTime = Calendar.getInstance();
//            beginTime.set(2012, 9, 14, 7, 30);
//            startMillis = beginTime.getTimeInMillis();
//            Calendar endTime = Calendar.getInstance();
//            endTime.set(2012, 9, 14, 8, 45);
//            endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, Calendar.getInstance().getTimeInMillis());
        values.put(Events.DTEND, Calendar.getInstance().getTimeInMillis() + 60 * 60 * 1000);
        values.put(Events.TITLE, "品爵 Reservation on " + Calendar.getInstance().getTime());
        values.put(Events.DESCRIPTION, "Reservation");
        values.put(Events.CALENDAR_ID, 1);
        values.put(Events.EVENT_LOCATION, "KL");
        values.put(Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        Uri uri = cr.insert(Events.CONTENT_URI, values);


        this._eventId = Long.parseLong(uri.getLastPathSegment());

        ContentValues values1 = new ContentValues();
        values1.put(CalendarContract.Reminders.MINUTES, 1440);
        values1.put(CalendarContract.Reminders.EVENT_ID, _eventId);
        values1.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri1 = cr.insert(CalendarContract.Reminders.CONTENT_URI, values1);
        Cursor c = CalendarContract.Reminders.query(cr, _eventId,
                new String[]{CalendarContract.Reminders.MINUTES});
        if (c.moveToFirst()) {
            System.out.println("calendar"
                    + c.getInt(c.getColumnIndex(CalendarContract.Reminders.MINUTES)));
        }
        c.close();
        Toast.makeText(getContext(), "event added to Calendar", Toast.LENGTH_SHORT).show();
    }


//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reservation, container, false);
        return view;
    }


    //    https://developer.android.com/guide/topics/providers/calendar-provider#add-event
//    https://www.youtube.com/watch?v=UbO4W3Wm-YY&ab_channel=AndroidOutlook
//    https://www.youtube.com/watch?v=GihhIgDYCNo&ab_channel=AndroidOutlook
//    https://www.youtube.com/watch?v=NK_-phxyIAM&ab_channel=CodingDemos
//    https://stackoverflow.com/a/10297387
//    https://stackoverflow.com/questions/16068082/how-can-i-add-event-to-the-calendar-automatically

}