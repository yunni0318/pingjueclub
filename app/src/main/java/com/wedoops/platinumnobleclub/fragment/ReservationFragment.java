package com.wedoops.platinumnobleclub.fragment;

import android.app.DatePickerDialog;
import android.content.ContentResolver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.CalendarContract;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wedoops.platinumnobleclub.CustomProgressDialog;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ReservationFragment extends Fragment implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {

    private static final int REQUEST_PERMISSIONS_CODE_WRITE_CALENDAR = 1;
    private View view;
    private Context app;
    private Spinner sp1, sp2;
    private TextView textview_calendar, textview_time;
    private DatePickerDialog datePickerDialog;
    private Button submit;
    private EditText name, username, contact;
    private com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog;
    private int Year, Month, Day, Hour, Minute;
    private long _eventId, reservationtime, reservationdate,convertmili;
    private String Date;
    private static CustomProgressDialog customDialog;


//    public ReservationFragment() {
//        // Required empty public constructor
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = getContext();
        customDialog = new CustomProgressDialog();

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
        List<String> roomtype = new ArrayList<String>();
        roomtype.add("Room Type");
        roomtype.add("1");
        roomtype.add("2");
        roomtype.add("3");
        roomtype.add("4");

        ArrayAdapter<String> adapter_roomtype = new ArrayAdapter<String>(getContext(), R.layout.spinner_layout, roomtype) {
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
        adapter_roomtype.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp1.setAdapter(adapter_roomtype);
        sp1.setSelection(0, false);


        List<String> pax = new ArrayList<String>();
        pax.add("Pax");
        pax.add("1");
        pax.add("2");
        pax.add("3");
        pax.add("4");

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
        sp2.setAdapter(adapter_pax);
        sp2.setSelection(0, false);

        return view;
    }

    private void setupDeclaration() {

        sp1 = view.findViewById(R.id.sp1);
        sp2 = view.findViewById(R.id.sp2);
        textview_calendar = view.findViewById(R.id.textview_calendar);
        textview_time = view.findViewById(R.id.textview_time);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        contact = view.findViewById(R.id.contact);
        submit = view.findViewById(R.id.btn_reservation);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                customDialog.showDialog(getContext());

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
                        ContentValues values = new ContentValues();
                        values.put(Events.DTSTART, reservationdate);
                        values.put(Events.DTEND, reservationtime);
                        values.put(Events.TITLE, "品爵 Reservation on " + Date);
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

                        customDialog.hideDialog();
                        Fragment fragment = new reservation_successFragment();
                        FragmentTransaction fragmentTransaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout_fragment_container, fragment, "NAMELISTFRAGMENT");
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                }, 2000);

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
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    }

    private void DateTimeDialogPicker() {

        final Calendar cldr = Calendar.getInstance();
        final int day = cldr.get(Calendar.DAY_OF_MONTH);
        final int month = cldr.get(Calendar.MONTH);
        final int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(getContext(), R.style.TimePickerTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        Date chosenDate = cal.getTime();
                        Date cldrDate = cldr.getTime();

                        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy");
                        String DateTime2 = newFormat.format(chosenDate);
                        Date = DateTime2;
                        reservationdate = cal.getTimeInMillis();
                        textview_calendar.setText(DateTime2);
                        timeDialogPicker();
//                        if (DateTime2.equals(DateTime3)) {
//                            reservationdate = DateTime2;
//                            textview_calendar.setText(reservationdate);
//                            timeDialogPicker();
//
//                        } else if (cldr.before(cal)) {
//                            reservationdate = DateTime2;
//                            textview_calendar.setText(reservationdate);
//                            timeDialogPicker();
//
//                        } else {
//                            Toast.makeText(app, "Invalid Date", Toast.LENGTH_LONG).show();
//                        }

                    }

                }, year, month, day);
        Calendar calen = Calendar.getInstance();

        calen.add(Calendar.DAY_OF_YEAR, +14);
        long after2week = calen.getTimeInMillis();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        datePickerDialog.getDatePicker().setMaxDate(after2week);
        datePickerDialog.show();

    }

    private void timeDialogPicker() {
        timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(ReservationFragment.this, Hour, Minute, false);
        timePickerDialog.setThemeDark(false);
        timePickerDialog.setStartTime(12, 00);
        //timePickerDialog.showYearPickerFirst(false);
        timePickerDialog.setTimeInterval(1, 30, 60);
        timePickerDialog.setAccentColor(ContextCompat.getColor(getContext(), R.color.colorPrimary_dark));
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


        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        if(timeSet == "PM"){
            hours = hours + 12;
        }
        long milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(mins));
        Date date = new Date(milliseconds);
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
        int count = sp1.getSelectedItemPosition();
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isvalidpax() {
        int count = sp2.getSelectedItemPosition();
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }


    //    https://developer.android.com/guide/topics/providers/calendar-provider#add-event
//    https://www.youtube.com/watch?v=UbO4W3Wm-YY&ab_channel=AndroidOutlook
//    https://www.youtube.com/watch?v=GihhIgDYCNo&ab_channel=AndroidOutlook
//    https://www.youtube.com/watch?v=NK_-phxyIAM&ab_channel=CodingDemos
//    https://stackoverflow.com/a/10297387
//    https://stackoverflow.com/questions/16068082/how-can-i-add-event-to-the-calendar-automatically

}