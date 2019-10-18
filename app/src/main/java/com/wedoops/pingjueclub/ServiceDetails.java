package com.wedoops.pingjueclub;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceDetails extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        textView=(TextView)findViewById(R.id.service_detail_text);
        imageView=(ImageView)findViewById(R.id.service_detail_iamge);

        Intent intent=getIntent();
        String title=intent.getExtras().getString("Title");
        String image=intent.getExtras().getString("Image");
        int thumbnail=intent.getExtras().getInt("Thumbnail");

        textView.setText(title);
        imageView.setImageResource(thumbnail);
    }
}
