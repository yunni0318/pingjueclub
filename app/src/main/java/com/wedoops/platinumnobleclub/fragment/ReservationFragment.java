package com.wedoops.platinumnobleclub.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.provider.CalendarContract.Events;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.StringUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wedoops.platinumnobleclub.CustomProgressDialog;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.RoomTypeSelection;
import com.wedoops.platinumnobleclub.database.Reservation_roomType;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class ReservationFragment extends Fragment implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private View view;
    private static Context app;
    private static Activity getactivity;
    private Spinner sp1, sp2;
    private TextView textview_calendar, textview_time, hall, room;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;
    private Button submit;
    private EditText name, username, contact;
    private com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog;
    private int Year, Month, Day, Hour, Minute;
    private long _eventId, reservationtime, reservationdate, convertmili;
    private String Date, Date_for_API, roomtype_for_api, EventTitle;
    private static CustomProgressDialog customDialog;
    private RelativeLayout relativelayout3_2, relativelayout3_1;


//    public ReservationFragment() {
//        // Required empty public constructor
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = getContext();
        customDialog = new CustomProgressDialog();
        getactivity = getActivity();
//        if (checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED &&
//                checkSelfPermission(getContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//
//            requestPermissions(new String[]{android.Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, REQUEST_PERMISSIONS_CODE_WRITE_CALENDAR);
//
//        }
    }


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

        setupDeclaration();

        textview_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeDialogPicker();
            }
        });

        textview_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeDialogPicker();
            }
        });

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_RetriveAllProduct);

        new CallWebServices(Api_Constants.API_RetriveAllProduct, view.getContext(), true).execute(b);

        List<String> pax = new ArrayList<String>();
        pax.add(getResources().getString(R.string.reservations_pax));

        for (int i = 1; i <= 30; i++) {
            pax.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter_pax = new ArrayAdapter<String>(getContext(), R.layout.spinner_layout, pax) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
//                view.setBackgroundColor(Color.BLACK);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);

                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        adapter_pax.setDropDownViewResource(R.layout.spinner_dropdown_item);
//        adapter_pax.setDropDownViewResource(android.R.layout.simple_spinner_item);

        sp2.setAdapter(adapter_pax);

        return view;
    }

    private void setupDeclaration() {

//        sp1 = view.findViewById(R.id.sp1);
        sp2 = view.findViewById(R.id.sp2);
        textview_calendar = view.findViewById(R.id.textview_calendar);
        textview_time = view.findViewById(R.id.textview_time);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        contact = view.findViewById(R.id.contact);
        submit = view.findViewById(R.id.btn_reservation);
        relativelayout3_1 = view.findViewById(R.id.relativelayout3_1);
        relativelayout3_2 = view.findViewById(R.id.relativelayout3_2);
        hall = view.findViewById(R.id.hall);
        room = view.findViewById(R.id.room);
        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);
        username.setText(ud.get(0).getName());
        username.setEnabled(false);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                customDialog.showDialog(getContext());

                EventTitle = "品爵 Reservation on " + Date;
                ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
                ContentValues values = new ContentValues();
                values.put(Events.DTSTART, reservationdate);
                values.put(Events.DTEND, reservationtime);
                values.put(Events.TITLE, EventTitle);
                values.put(Events.DESCRIPTION, "Reservation");
                values.put(Events.CALENDAR_ID, 1);
                values.put(Events.EVENT_LOCATION, "KL");
                values.put(Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
                Uri uri = cr.insert(Events.CONTENT_URI, values);
                _eventId = Long.parseLong(uri.getLastPathSegment());

//                ContentValues values1 = new ContentValues();
//                values1.put(CalendarContract.Reminders.MINUTES, 1440);
//                values1.put(CalendarContract.Reminders.EVENT_ID, _eventId);
//                values1.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
//                Uri uri1 = cr.insert(CalendarContract.Reminders.CONTENT_URI, values1);
//                Cursor c = CalendarContract.Reminders.query(cr, _eventId,
//                        new String[]{CalendarContract.Reminders.MINUTES});
//                if (c.moveToFirst()) {
//                    System.out.println("calendar"
//                            + c.getInt(c.getColumnIndex(CalendarContract.Reminders.MINUTES)));
//                }
//                c.close();
//                Toast.makeText(getContext(), "event added to Calendar", Toast.LENGTH_SHORT).show();

                List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

                String table_name = UserDetails.getTableName(UserDetails.class);
                String loginid_field = StringUtil.toSQLName("LoginID");

                List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());


                Bundle b = new Bundle();
                b.putString("ProductGUID", roomtype_for_api);
                b.putString("ReservationName", ud_list.get(0).getName());
                b.putString("ReservationDate", Date_for_API);
                b.putString("NumberPax", String.valueOf(sp2.getSelectedItemPosition()));
                b.putString("Remark", "");

                b.putString("access_token", ud_list.get(0).getAccessToken());
                b.putInt(Api_Constants.COMMAND, Api_Constants.API_MakeReservation);

                new CallWebServices(Api_Constants.API_MakeReservation, view.getContext(), true).execute(b);

                customDialog.hideDialog();
//                Fragment fragment = new reservation_successFragment();
//                FragmentTransaction fragmentTransaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.framelayout_fragment_container, fragment, "NAMELISTFRAGMENT");
//                fragmentTransaction.commitAllowingStateLoss();

//            final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//                }, 2000);

//
//                reservation_successFragment reservation_successFragment = new reservation_successFragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.framelayout_fragment_container, reservation_successFragment);
//                fragmentTransaction.addToBackStack(null);

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (validateUsername() & validatename() & isValidDate() & isValidMobile() & isValidTime() & isvalidroom() & isvalidpax()) {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_selected));
                    submit.setEnabled(true);
                } else {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_unselected));
                    submit.setTextColor(getResources().getColor(R.color.white));
                    submit.setEnabled(false);
                }
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (validateUsername() & validatename() & isValidDate() & isValidMobile() & isValidTime() & isvalidroom() & isvalidpax()) {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_selected));
                    submit.setEnabled(true);
                } else {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_unselected));
                    submit.setTextColor(getResources().getColor(R.color.white));
                    submit.setEnabled(false);
                }
            }
        });
        textview_calendar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (validateUsername() & validatename() & isValidDate() & isValidMobile() & isValidTime() & isvalidroom() & isvalidpax()) {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_selected));
                    submit.setEnabled(true);
                } else {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_unselected));
                    submit.setTextColor(getResources().getColor(R.color.white));

                    submit.setEnabled(false);
                }
            }
        });
        textview_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (validateUsername() & validatename() & isValidDate() & isValidMobile() & isValidTime() & isvalidroom() & isvalidpax()) {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_selected));
                    submit.setEnabled(true);
                } else {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_unselected));
                    submit.setTextColor(getResources().getColor(R.color.white));

                    submit.setEnabled(false);
                }
            }
        });
        contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (validateUsername() & validatename() & isValidDate() & isValidMobile() & isValidTime() & isvalidroom() & isvalidpax()) {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_selected));
                    submit.setEnabled(true);
                } else {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_unselected));
                    submit.setTextColor(getResources().getColor(R.color.white));

                    submit.setEnabled(false);
                }
            }
        });
//        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                if (validateUsername() & validatename() & isValidDate() & isValidMobile() & isValidTime() & isvalidroom() & isvalidpax()) {
//                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_selected));
//                    submit.setEnabled(true);
//                } else {
//                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_unselected));
//                    submit.setTextColor(getResources().getColor(R.color.white));
//
//                    submit.setEnabled(false);
//                }
//
//                String input = sp1.getItemAtPosition(position).toString();
//                input = input.replace("     - ", "");
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (validateUsername() & validatename() & isValidDate() & isValidMobile() & isValidTime() & isvalidroom() & isvalidpax()) {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_selected));
                    submit.setEnabled(true);
                } else {
                    submit.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_reservation_unselected));
                    submit.setTextColor(getResources().getColor(R.color.white));
                    submit.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        relativelayout3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativelayout3_1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.reservation_roomtype_background_selected));
                relativelayout3_2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.reservation_roomtype_background));
                hall.setTextColor(Color.WHITE);
                room.setTextColor(Color.parseColor("#AAAAAA"));
                room.setText("Private Room");


                List<Reservation_roomType> mbl = Reservation_roomType.listAll(Reservation_roomType.class);

                String table_name = Reservation_roomType.getTableName(Reservation_roomType.class);
                String loginid_field = StringUtil.toSQLName("ProductCatagory");

                List<Reservation_roomType> ud_list = Reservation_roomType.findWithQuery(Reservation_roomType.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", "HALL");
                roomtype_for_api = ud_list.get(0).getProductGUID();

            }
        });
        relativelayout3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativelayout3_2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.reservation_roomtype_background_selected));
                relativelayout3_1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.reservation_roomtype_background));
                room.setTextColor(Color.WHITE);
                hall.setTextColor(Color.parseColor("#AAAAAA"));
                Intent intent = new Intent(getContext(), RoomTypeSelection.class);
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
//                roomtitlefromRoomSelection  = data.getStringExtra("roomtitle");
                roomtype_for_api = data.getStringExtra("productGUID");

                room.setText(data.getStringExtra("roomtitle"));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    private void DateTimeDialogPicker() {

        // date picker dialog
        datePickerDialog = DatePickerDialog.newInstance(ReservationFragment.this, Year, Month, Day);
        datePickerDialog.setThemeDark(false);
        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));


        // Setting Min Date to today date
        Calendar min_date_c = Calendar.getInstance();
        datePickerDialog.setMinDate(min_date_c);


        // Setting Max Date to next 2 years
        Calendar max_date_c = Calendar.getInstance();
        max_date_c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        max_date_c.add(Calendar.DATE, 15); //2 week after

        datePickerDialog.setMaxDate(max_date_c);



        //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
        for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
            int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY) {
                Calendar[] disabledDays =  new Calendar[1];
                disabledDays[0] = loopdate;
                datePickerDialog.setDisabledDays(disabledDays);
            }
        }


        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

//        datePickerDialog = new DatePickerDialog(getContext(), R.style.TimePickerTheme,
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        Year = year;
//                        Month = monthOfYear;
//                        Day = dayOfMonth;
//
//                        Calendar cal = Calendar.getInstance();
//                        cal.setTimeInMillis(0);
//                        cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
//                        Date chosenDate = cal.getTime();
//                        Date cldrDate = cldr.getTime();
//                        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        String DateTime = newFormat.format(chosenDate);
//                        SimpleDateFormat newFormat1 = new SimpleDateFormat("dd MMM yyyy");
//                        String DateTime2 = newFormat1.format(chosenDate);
//                        Date = DateTime2;
////                        Date_for_API =  DateTime;
//                        reservationdate = cal.getTimeInMillis();
//                        textview_calendar.setText(DateTime2);
//                        timeDialogPicker();
////                        if (DateTime2.equals(DateTime3)) {
////                            reservationdate = DateTime2;
////                            textview_calendar.setText(reservationdate);
////                            timeDialogPicker();
////
////                        } else if (cldr.before(cal)) {
////                            reservationdate = DateTime2;
////                            textview_calendar.setText(reservationdate);
////                            timeDialogPicker();
////
////                        } else {
////                            Toast.makeText(app, "Invalid Date", Toast.LENGTH_LONG).show();
////                        }
//
//                    }
//
//                }, year, month, day);
//
//        //set Dates of start and end of current week
//        Calendar calen = Calendar.getInstance();
//        calen.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//        calen.add(Calendar.DATE, 14); //2 week after
//        long after2week = calen.getTimeInMillis();
//
//        //if tomorrow  need add  +  (System.currentTimeMillis() 24 * 60 * 60 * 1000)
//        datePickerDialog.setMinDate(System.currentTimeMillis());
//        datePickerDialog.setMaxDate(after2week);
//        datePickerDialog.show();

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
        Date chosenDate = cal.getTime();

        SimpleDateFormat newFormat1 = new SimpleDateFormat("dd MMM yyyy");
        String DateTime2 = newFormat1.format(chosenDate);
        Date = DateTime2;
        reservationdate = cal.getTimeInMillis();
        textview_calendar.setText(DateTime2);

        timeDialogPicker();

    }

    private void timeDialogPicker() {
        timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(ReservationFragment.this, Hour, Minute, false);
        timePickerDialog.setThemeDark(false);

        timePickerDialog.setStartTime(16, 00);
        Calendar calendar = GregorianCalendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date now = new Date();
        if (now.compareTo(calendar.getTime()) < 0) {
            timePickerDialog.setMinTime(16, 0, 0);
        }else{
            Calendar calendar1 = GregorianCalendar.getInstance();
            timePickerDialog.setMinTime(calendar1.getTime().getHours(),calendar1.getTime().getMinutes(),calendar1.getTime().getSeconds());
        }
//        timePickerDialog.setMinTime(16, 0, 0);
        timePickerDialog.setMaxTime(23, 59, 0);

        //timePickerDialog.showYearPickerFirst(false);
//        timePickerDialog.setTimeInterval(1, 30, 60);
        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary_dark));
//
//        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//
//                Toast.makeText(getContext(), "Timepicker Canceled", Toast.LENGTH_SHORT).show();
//            }
//        });

        timePickerDialog.show(getFragmentManager(), "TimePickerDialog");

    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        updateTime(hourOfDay, minute);
    }

    private void updateTime(int hours, int mins) {
        String strMin = "";
        String timeSet = "";
        String m = "", s = "", d = "", h = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        //=====Adding zero in one dightleft  and right======
        if (mins < 10)
            strMin = "0" + mins;
        else
            strMin = String.valueOf(mins);
        // Append in a StringBuilder
        String aTime = new StringBuilder().append(pad(hours)).append(':')
                .append(pad(Integer.parseInt(strMin))).append(" ").append(timeSet).toString();

        Log.e("aTime checking ==>", "" + aTime);
//       2021-04-13T08:20:17.974Z",
//         2021-3-14T12:00:0.000Z

        if (Month < 10) {
            m = "0" + Month;
        } else {
            m = String.valueOf(Month);
        }
        if (mins < 10) {
            s = "0" + mins;
        } else {
            s = String.valueOf(mins);
        }
        if (Day < 10) {
            d = "0" + Day;
        } else {
            d = String.valueOf(Day);
        }
        if (hours < 10) {
            h = "0" + hours;
        } else {
            h = String.valueOf(hours);
        }
        if (timeSet == "PM") {
            h = String.valueOf(hours + 12);
        }
        Date_for_API = Year + "-" + m + "-" + d + "T" + h + ":" + s + ":00" + ".000Z";
        String a3 = Year + "-" + m + "-" + d + " " + h + ":" + s;

        Date a = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

            a = sdf.parse(a3);
            sdf.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date_for_API = sdf.format(a);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(Long.parseLong(h)) + TimeUnit.MINUTES.toSeconds(mins));
        reservationdate = reservationdate + milliseconds;
        reservationtime = reservationdate + 120 * 60 * 1000;
        textview_time.setText(aTime);
    }

    private String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private boolean validateUsername() {
        String username1 = username.getText().toString().trim();

        if (username1.isEmpty()) {
            name.setError(null);
            return false;
        } else if (username1.length() < 6) {
            username.setError(getResources().getString(R.string.Reservation_name_Length_error));
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private boolean validatename() {
        String name1 = name.getText().toString().trim();

        if (name1.isEmpty()) {
            name.setError(null);
            return false;
        } else if (name1.length() < 6) {
            name.setError(getResources().getString(R.string.Reservation_name_Length_error));
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidMobile() {
        String phoneNumber = contact.getText().toString().trim();

        if (phoneNumber.equals("")) {
            return false;
        } else {
            if (!phoneNumber.startsWith("01")) {
                contact.setError(getResources().getString(R.string.Reservation_contact_error));
                return false;
            } else {
                if (phoneNumber.length() >= 10 && phoneNumber.length() <= 11) {
                    contact.setError(null);
                    return true;
                } else {
                    contact.setError(getResources().getString(R.string.Reservation_contact_Length_error));
                    return false;
                }
            }
        }
    }

    private boolean isValidTime() {
        String Time = textview_time.getText().toString().trim();

        if (Time.isEmpty()) {
//            textview_time.setError(getResources().getString(R.string.Reservation_name_Length_error));
            return false;
        } else {
            textview_time.setError(null);
            return true;
        }

    }

    private boolean isValidDate() {
        String Date = textview_calendar.getText().toString().trim();
        if (Date.isEmpty()) {
//            textview_calendar.setError(getResources().getString(R.string.Reservation_name_Length_error));
            return false;
        } else {
            textview_calendar.setError(null);
            return true;
        }
    }

    private boolean isvalidroom() {
//        int count = sp1.getSelectedItemPosition();
//        if (count == 0) {
//            return false;
//        } else {
        return true;
//        }
    }

    private boolean isvalidpax() {
        int count = sp2.getSelectedItemPosition();
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static void processWSData(JSONObject returnedObject, int command) {
//        customProgress.dismiss();
        customDialog.hideDialog();

        if (command == Api_Constants.API_MakeReservation) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {
                    if (returnedObject.getInt("StatusCode") == 200) {

//                        {"Success":true,"StatusCode":200,"ErrorCode":[],"ResponseData":{}}
//                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");
//                        new DisplayAlertDialog().displayAlertDialogSuccess(returnedObject.getInt("StatusCode"), app, getactivity);

                        Fragment fragment = new reservation_successFragment();
                        FragmentTransaction fragmentTransaction = ((FragmentActivity) app).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout_fragment_container, fragment, "NAMELISTFRAGMENT");
                        fragmentTransaction.commitAllowingStateLoss();

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), app, getactivity);
                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, app, getactivity);
            }
        }
        if (command == Api_Constants.API_RetriveAllProduct) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {
                    if (returnedObject.getInt("StatusCode") == 200) {
                        JSONArray response_object = returnedObject.getJSONArray("ResponseData");
//                        JSONObject success_object = response_object.getJSONObject("SuccessMessage");
//                        JSONObject profile_object = response_object.getJSONObject("UserQuickProfile");

                        List<Reservation_roomType> reservation_roomTypes = Reservation_roomType.listAll(Reservation_roomType.class);
                        if (reservation_roomTypes.size() > 0)
                            Reservation_roomType.deleteAll(Reservation_roomType.class);

                        for (int i = 0; i < response_object.length(); i++) {
                            JSONObject object1 = response_object.getJSONObject(i);
                            String ProductGUID = object1.getString("ProductGUID");
                            String ProductName = object1.getString("ProductName");
                            String ProductCatagory = object1.getString("ProductCatagory");
                            String Category = object1.getString("Category");
                            String ProductDescription = object1.getString("ProductDescription");
                            String ProductImage = object1.getString("ProductImage");
                            int EstimateParticipant = object1.getInt("EstimateParticipant");


                            Reservation_roomType roomType = new Reservation_roomType(ProductGUID, ProductName, ProductCatagory, Category, ProductDescription, ProductImage, EstimateParticipant);
                            roomType.save();
                        }
//                        new DisplayAlertDialog().displayAlertDialogSuccess(response_object.getInt("Code"), app, getactivity);


                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), app, getactivity);
                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, app, getactivity);
            }
        }


    }




    //    https://developer.android.com/guide/topics/providers/calendar-provider#add-event
//    https://www.youtube.com/watch?v=UbO4W3Wm-YY&ab_channel=AndroidOutlook
//    https://www.youtube.com/watch?v=GihhIgDYCNo&ab_channel=AndroidOutlook
//    https://www.youtube.com/watch?v=NK_-phxyIAM&ab_channel=CodingDemos
//    https://stackoverflow.com/a/10297387
//    https://stackoverflow.com/questions/16068082/how-can-i-add-event-to-the-calendar-automatically

}