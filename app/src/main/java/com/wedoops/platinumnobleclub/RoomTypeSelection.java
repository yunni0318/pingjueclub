package com.wedoops.platinumnobleclub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.adapters.MyBookingAdapter;
import com.wedoops.platinumnobleclub.adapters.RoomTypeSelectionAdapter;
import com.wedoops.platinumnobleclub.database.MyBookingList;
import com.wedoops.platinumnobleclub.database.Reservation_roomType;
import com.wedoops.platinumnobleclub.database.UserDetails;

import java.util.List;

public class RoomTypeSelection extends AppCompatActivity implements RoomTypeSelectionAdapter.OnClickListener {

    private RecyclerView recyclerView;
    private RoomTypeSelectionAdapter roomTypeSelectionAdapter;
    private static RoomTypeSelectionAdapter.OnClickListener onClickListener;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_type_selection);
        recyclerView = findViewById(R.id.recyclerView);
        onClickListener = this;

        List<Reservation_roomType> mbl = Reservation_roomType.listAll(Reservation_roomType.class);

        String table_name = Reservation_roomType.getTableName(Reservation_roomType.class);
        String loginid_field = StringUtil.toSQLName("ProductCatagory");

        List<Reservation_roomType> ud_list = Reservation_roomType.findWithQuery(Reservation_roomType.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", "ROOM");


        roomTypeSelectionAdapter = new RoomTypeSelectionAdapter(ud_list,onClickListener,this);
        RecyclerView.LayoutManager booking_list_mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(booking_list_mLayoutManager);
        recyclerView.setAdapter(roomTypeSelectionAdapter);


    }
    @Override
    public void onClickListener(String roomtitle,String productGUID) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("roomtitle",roomtitle);
        returnIntent.putExtra("productGUID",productGUID);

        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void button_back(View view) {
        finish();
    }
}