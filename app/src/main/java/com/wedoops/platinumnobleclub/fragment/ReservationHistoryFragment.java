package com.wedoops.platinumnobleclub.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.adapters.ReservationHistoryAdapter;
import com.wedoops.platinumnobleclub.database.EventDetailBookingData;
import com.wedoops.platinumnobleclub.database.MyBookingList;
import com.wedoops.platinumnobleclub.database.Reservation_History;
import com.wedoops.platinumnobleclub.database.Reservation_roomType;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ReservationHistoryFragment extends Fragment {

    private static Context get_context;
    private static RecyclerView recyclerview_bookingdata;
    private static View view;
    private static Activity getActivity;
    private static ReservationHistoryAdapter reservationHistoryAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reservation_history_fragment, container, false);
        get_context = getContext();
        getActivity = getActivity();


        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());


        Bundle b = new Bundle();


        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_MyReservationList);

        new CallWebServices(Api_Constants.API_MyReservationList, view.getContext(), true).execute(b);

//        reservationHistoryAdapter.setOnBookingListItemClickListener(onMyBookingItemClickListener);
        return view;
    }

    public static void processWSData(JSONObject returnedObject, int command) {
//        customProgress.dismiss();
//        customDialog.hideDialog();

        if (command == Api_Constants.API_MyReservationList) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {
                    if (returnedObject.getInt("StatusCode") == 200) {
                        JSONArray response_object = returnedObject.getJSONArray("ResponseData");

                        List<Reservation_History> reservationHistories = Reservation_History.listAll(Reservation_History.class);
                        if (reservationHistories.size() > 0)
                            Reservation_History.deleteAll(Reservation_History.class);

                        for (int i = 0; i < response_object.length(); i++) {
                            JSONObject object1 = response_object.getJSONObject(i);

                            int Srno = object1.getInt("Srno");
                            String ProductGUID = object1.getString("ProductGUID");
                            String ReservationName = object1.getString("ReservationName");
                            int EstimateParticipant = object1.getInt("EstimateParticipant");
                            String ReservationNumber = object1.getString("ReservationNumber");
                            String Remark = object1.getString("Remark");
                            String ProductName = object1.getString("ProductName");
                            String Category = object1.getString("Category");
                            String ProductDescription = object1.getString("ProductDescription");
                            String ProductImage = object1.getString("ProductImage");
                            String UserLevelCode = object1.getString("UserLevelCode");
                            String ReservationDate = object1.getString("ReservationtDate");
                            String ReservationStatus = object1.getString("ReservationStatus");
                            String CreatedBy = object1.getString("CreatedBy");
                            String CreatedDate = object1.getString("CreatedDate");


                            Reservation_History reservation_history = new Reservation_History(Srno, ProductGUID, ReservationName, EstimateParticipant, ReservationNumber, Remark, ProductName, Category,
                                    ProductDescription, ProductImage, UserLevelCode, ReservationDate, ReservationStatus, CreatedBy, CreatedDate);
                            reservation_history.save();
                        }

                        List<Reservation_History> mbl1 = Reservation_History.listAll(Reservation_History.class);
                        String table_name = Reservation_History.getTableName(Reservation_History.class);
                        String ReservationtDate = StringUtil.toSQLName("ReservationtDate");
                        String ReservationStatus = StringUtil.toSQLName("ReservationStatus");

                        //will follow order by status -> date
                        List<Reservation_History> ud_list = Reservation_History.findWithQuery(Reservation_History.class, "SELECT * from " + table_name + "  ORDER BY " + ReservationStatus + " ASC, " + ReservationtDate + " DESC ", null);

                        recyclerview_bookingdata = view.findViewById(R.id.recyclerview_bookingdata);

                        reservationHistoryAdapter = new ReservationHistoryAdapter(ud_list, get_context);

                        RecyclerView.LayoutManager booking_list_mLayoutManager = new LinearLayoutManager(view.getContext());
                        recyclerview_bookingdata.setLayoutManager(booking_list_mLayoutManager);

                        recyclerview_bookingdata.setAdapter(reservationHistoryAdapter);
                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), get_context, getActivity);
                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, get_context, getActivity);
            }
        }
    }

}
