package com.wedoops.platinumnobleclub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wedoops.platinumnobleclub.adapters.MyBookingAdapter;
import com.wedoops.platinumnobleclub.adapters.RoomTypeSelectionAdapter;
import com.wedoops.platinumnobleclub.database.MyBookingList;

import java.util.List;

public class RoomTypeSelection extends AppCompatActivity implements RoomTypeSelectionAdapter.OnClickListener {

    private RecyclerView recyclerView;
    private RoomTypeSelectionAdapter roomTypeSelectionAdapter;
    private static RoomTypeSelectionAdapter.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_type_selection);
        recyclerView = findViewById(R.id.recyclerView);
        onClickListener = this;

        List<MyBookingList> mbl = MyBookingList.listAll(MyBookingList.class);

        roomTypeSelectionAdapter = new RoomTypeSelectionAdapter(mbl,onClickListener);
        RecyclerView.LayoutManager booking_list_mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(booking_list_mLayoutManager);
        recyclerView.setAdapter(roomTypeSelectionAdapter);


    }
    @Override
    public void onClickListener(String roomtitle) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",roomtitle);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}