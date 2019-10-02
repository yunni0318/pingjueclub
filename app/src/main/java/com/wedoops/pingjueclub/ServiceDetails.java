package com.wedoops.pingjueclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ServiceDetails extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        textView=(TextView)findViewById(R.id.service_detail_text);

        Intent intent=getIntent();
        String title=intent.getExtras().getString("Title");
        String image=intent.getExtras().getString("Image");

        textView.setText(title);
    }
}
