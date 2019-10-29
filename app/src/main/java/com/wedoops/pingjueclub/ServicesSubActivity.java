package com.wedoops.pingjueclub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orm.StringUtil;
import com.wedoops.pingjueclub.adapters.ServiceSubItemAdapter;
import com.wedoops.pingjueclub.database.SubServicesListData;

import java.util.List;

public class ServicesSubActivity extends Activity {

    private RecyclerView recyclerview_sub;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private String mainservicesid, mainservicesname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_sub_activity);

        Intent intent = getIntent();
        mainservicesid = intent.getStringExtra("main_services_id");
        mainservicesname = intent.getStringExtra("main_services_name");

        setupDeclaration();
        setupRecyclerView(mainservicesid);
    }

    private void setupDeclaration() {
        recyclerview_sub = findViewById(R.id.recyclerview_sub);
        toolbar_back = findViewById(R.id.toolbar_back);
        toolbar_title = findViewById(R.id.toolbar_title);

        toolbar_title.setText(mainservicesname);

        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setupRecyclerView(String id) {
        String tablename_ssld = StringUtil.toSQLName("SubServicesListData");
        String fieldname_foreign_main_id = StringUtil.toSQLName("ForeignMainServicesID");

        List<SubServicesListData> ssld_list_all = SubServicesListData.findWithQuery(SubServicesListData.class, "SELECT * FROM " + tablename_ssld + " WHERE " + fieldname_foreign_main_id + " = '" + id + "'");


//        RecyclerView.LayoutManager sub_mLayoutManager = new LinearLayoutManager(this);
        ServiceSubItemAdapter adapter = new ServiceSubItemAdapter(this, ssld_list_all);

        recyclerview_sub.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerview_sub.setNestedScrollingEnabled(false);
        recyclerview_sub.setAdapter(adapter);
    }
}
